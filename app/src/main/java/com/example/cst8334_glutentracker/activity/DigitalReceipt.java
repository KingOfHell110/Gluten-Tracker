package com.example.cst8334_glutentracker.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cst8334_glutentracker.CartListViewHolder;
import com.example.cst8334_glutentracker.R;
import com.example.cst8334_glutentracker.database.GlutenDatabase;
import com.example.cst8334_glutentracker.entity.Product;
import com.example.cst8334_glutentracker.entity.Receipt;

import java.util.ArrayList;

public class DigitalReceipt extends AppCompatActivity {
    private ProductAdapter adapter;
    private ArrayList<Product> products;
    private ListView productList;
    private SQLiteDatabase database;
    private GlutenDatabase dbOpener = new GlutenDatabase(this);
    TextView rrid;
    TextView rdate;
    TextView ded;
    Intent fromActivity;
    public static long passedIndex;
    Receipt receipt;



    static Product productToPass;



    static int index;

    public static long getPassedIndex() {
        return passedIndex;
    }

    public static void setPassedIndex(long index) {
        passedIndex = index;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_receipt);

        fromActivity=getIntent();
//      passedIndex=fromActivity.getIntExtra("index",);
        productList=findViewById(R.id.products);
        products=new ArrayList<>();
        rrid=findViewById(R.id.ridf);
        rdate=findViewById(R.id.datef);
        ded=findViewById(R.id.dedf);
//        productList.setOnItemLongClickListener((adapterView, view, i, l) -> {
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(adapterView.getContext());
//            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
//                switch (which){
//                    case DialogInterface.BUTTON_POSITIVE:
//                        break;
//
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        adapter.notifyDataSetChanged();
//                        break;
//                }
//            };
//            alertDialog.setTitle("Delete Product");
//            alertDialog.setMessage("Do you want to delete this product");
//            alertDialog.setNegativeButton("Delete", dialogClickListener);
//            alertDialog.setPositiveButton("No", dialogClickListener);
//            alertDialog.show();
//            return true;
//        })
        loadFromDatabase();
        rrid.setText(receipt.getId()+"");
        rdate.setText(receipt.getDate());
        ded.setText(Double.toString(receipt.getTaxDeductionTotal()));
        adapter=new ProductAdapter(products,this);
        productList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadFromDatabase(){
        receipt=dbOpener.selectReceiptByID(passedIndex);
        products.addAll(receipt.getProducts());
        /*        database = dbOpener.getReadableDatabase();
        Cursor rc = database.query(false, DatabaseActivity.Receipts.TABLE_NAME, new String[]{DatabaseActivity.Receipts.COLUMN_NAME_ID, DatabaseActivity.Receipts.COLUMN_NAME_FILE, DatabaseActivity.Receipts.COLUMN_NAME_DATE, DatabaseActivity.Receipts.COLUMN_NAME_TOTAL_DEDUCTION}, "receiptID=?",new String[]{Integer.toString(passedIndex)}, null, null, null, null, null);

        int idIndex=rc.getColumnIndex(DatabaseActivity.Receipts.COLUMN_NAME_ID);
        int dateIndex= rc.getColumnIndex(DatabaseActivity.Receipts.COLUMN_NAME_DATE);
        int deductionIndex= rc.getColumnIndex(DatabaseActivity.Receipts.COLUMN_NAME_TOTAL_DEDUCTION);

        while(rc.moveToNext()){
            String rid= rc.getString(idIndex);
            String date=rc.getString(dateIndex);
            Double deduction=rc.getDouble(deductionIndex);

            rrid.setText(rid);
            rdate.setText(date);
            ded.setText(Double.toString(deduction));
        }
//        Cursor pc = database.query(false, databaseActivity.Products.TABLE_NAME, new String[]{databaseActivity.Products.COLUMN_NAME_ID,databaseActivity.Products.COLUMN_NAME_PNAME,databaseActivity.Products.COLUMN_NAME_DESCRIPTION,databaseActivity.Products.COLUMN_NAME_GLUTEN,databaseActivity.Products.COLUMN_NAME_PRICE}, "receiptID=?",new String[]{Integer.toString(passedIndex)}, null, null, null, null, null);
        Cursor pc=database.rawQuery("SELECT * FROM "+ DatabaseActivity.Products.TABLE_NAME+ " JOIN " +
                DatabaseActivity.ProductReceipt.TABLE_NAME+" ON products."+ DatabaseActivity.Products.COLUMN_NAME_ID+" = productReceipt." +
                DatabaseActivity.ProductReceipt.COLUMN_NAME_PRODUCT_ID+" WHERE "+
                DatabaseActivity.ProductReceipt.COLUMN_NAME_RECEIPT_ID+" = ? ",new String[]{Integer.toString(passedIndex)});

        int pid=pc.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_ID);
        int nameIndex=pc.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_PRODUCT_NAME);
        int descIndex=pc.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_DESCRIPTION);
        int glutenIndex=pc.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_GLUTEN);

//      int dedIndex=pc.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_PRICE);

         int dedIndex=pc.getColumnIndex(DatabaseActivity.ProductReceipt.COLUMN_NAME_PRICE);
         int quantityIndex=pc.getColumnIndex(DatabaseActivity.ProductReceipt.COLUMN_NAME_QUANTITY);

        while(pc.moveToNext()){
            boolean glutenf=false;
            int productId=pc.getInt(pid);
            String name=pc.getString(nameIndex);
            String desc=pc.getString(descIndex);
            int gluten=pc.getInt(glutenIndex);
            int quantity=pc.getInt(quantityIndex);

            if(gluten==0)
            {glutenf=false;}
            if(gluten==1)
            if(gluten==1)
            {glutenf=true;}

            double ded=pc.getDouble(dedIndex);

            products.add(new Product(productId,name,desc,ded,glutenf).setQuantity(quantity));
        }

         */
    }

    public static Product getProductToPass() {
        return productToPass;
    }

    public static void setProductToPass(Product productToPass) {
        DigitalReceipt.productToPass = productToPass;
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        DigitalReceipt.index = index;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(getProductToPass() != null){
            products.set(getIndex(),getProductToPass());
            setProductToPass(null);
            setIndex(0);
            adapter.notifyDataSetChanged();
        }
    }

    public class ProductAdapter extends ArrayAdapter<Product> {
        private ArrayList<Product> rData;

        Context mContext;
        TextView proid;
        TextView name;
        TextView desc;
        TextView gluten;
        TextView quantity;
        TextView ded;
        Button edit;
        Button link;

        public ProductAdapter(ArrayList<Product> data, Context context)  {
            super(context,R.layout.digital_receipt_layout,data);
            this.rData=data;
            this.mContext=context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Product product = getItem(position);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.digital_receipt_layout, parent, false);
            proid = (TextView) convertView.findViewById(R.id.proid);
            name = (TextView) convertView.findViewById(R.id.pname);
            desc = (TextView) convertView.findViewById(R.id.pdesc);
            gluten = (TextView) convertView.findViewById(R.id.gluten);
            quantity = (TextView) convertView.findViewById(R.id.qty);
            ded = (TextView) convertView.findViewById(R.id.prize);
            edit = convertView.findViewById(R.id.edit);
            link = convertView.findViewById(R.id.lnk);
            if(product.getLinkedProduct()==null)
                link.setText("Link");
            else
                link.setText("Linked");

            if(!product.isGlutenFree())
                link.setEnabled(false);

            Context context=convertView.getContext();

            proid.setText(Long.toString(product.getId()));
            name.setText(product.getProductName());
            desc.setText(product.getProductDescription());
            if(product.getLinkedProduct()!=null)
                gluten.setText(product.getLinkedProduct().getProductName());
            quantity.setText(Integer.toString(product.getQuantity()));
            ded.setText(product.getDisplayedPriceAsString());
//            ded.setText(product.getPrice()+"");


            edit.setOnClickListener((v) -> {
                View row = getLayoutInflater().inflate(R.layout.activity_edit_receipt, parent, false);
                CartListViewHolder.editProduct(DigitalReceipt.this,product,adapter,row,dbOpener,passedIndex);
              /*  //Product editedProduct = product;
                Product editedProduct = new Product(product.getId(), product.getProductName(), product.getProductDescription(),
                        product.getPrice(), product.isGlutenFree());
                editedProduct.setQuantity(product.getQuantity());
                editedProduct.setDisplayedPrice(product.getDisplayedPrice());
                if(product.getLinkedProduct() != null) {
                    editedProduct.setLinkedProduct(product.getLinkedProduct());
                    editedProduct.getLinkedProduct().setQuantity(product.getLinkedProduct().getQuantity());
                    editedProduct.getLinkedProduct().setDisplayedPrice(editedProduct.getLinkedProduct().getPrice() * editedProduct.getLinkedProduct().getQuantity());
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            product.setQuantity(editedProduct.getQuantity());
                            product.setPrice(editedProduct.getPrice());
                            product.setDisplayedPrice(editedProduct.getDisplayedPrice());
                            if(product.getLinkedProduct() != null && editedProduct.getLinkedProduct() != null){
                                product.setLinkedProduct(editedProduct.getLinkedProduct());
                            }
                            SQLiteDatabase db = dbOpener.getWritableDatabase();
                            dbOpener.updateProductById(product);
                            adapter.notifyDataSetChanged();
                            break;
                    }
                };
                alertDialog.setTitle(editedProduct.getProductName());
//                alertDialog.setMessage(editedProduct.getProductName());
                alertDialog.setNegativeButton("Save", dialogClickListener).
                        setPositiveButton("Cancel", dialogClickListener);
                //EditText test = new EditText(context);
                //alertDialog.setView(test);
                //View row = getLayoutInflater().inflate(R.layout.activity_product_list, parent, false); // worked
                View row = getLayoutInflater().inflate(R.layout.activity_edit_receipt, parent, false);
                TextView deductibleEdit = row.findViewById(R.id.deductibleTextEdit);
                TextView deductibleViewEdit = row.findViewById(R.id.deductibleViewEdit);
                if(editedProduct.getLinkedProduct() == null){
                    deductibleViewEdit.setVisibility(row.INVISIBLE);
                    deductibleEdit.setVisibility(row.INVISIBLE);
                }
                else{
                    deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                }
                EditText changePriceEdit = row.findViewById(R.id.changePriceAndQuantityTextEdit);
                Button changePriceEditButton = row.findViewById(R.id.changePriceEdit);
                TextView priceEdit = row.findViewById(R.id.priceEdit);
                //priceEdit.setText(product.getDisplayedPrice() + ""); originally there
                priceEdit.setText(editedProduct.getDisplayedPrice() + "");
                EditText quantityEdit = row.findViewById(R.id.quantityEdit);
                quantityEdit.setText(editedProduct.getQuantity() + "");
                changePriceEditButton.setOnClickListener((view) -> {

                    if(changePriceEdit.getText().toString().trim().length() > 0) {
                        editedProduct.setQuantity(1);
                        editedProduct.setPrice(Double.valueOf(changePriceEdit.getText().toString()));
                        //quantity.setText(Integer.toString(product.getQuantity()));
                        quantityEdit.setText(Integer.toString(1));
                        editedProduct.setDisplayedPrice(editedProduct.getPrice() * editedProduct.getQuantity());
                        priceEdit.setText(editedProduct.getDisplayedPrice() + "");
                        changePriceEdit.setText("");
                        if (editedProduct.getLinkedProduct() != null) {
                            editedProduct.getLinkedProduct().setQuantity(editedProduct.getQuantity());
                            editedProduct.getLinkedProduct().setDisplayedPrice(editedProduct.getLinkedProduct().getPrice() * editedProduct.getLinkedProduct().getQuantity());
                            //deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                            deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                //EditText quantityEdit = row.findViewById(R.id.quantityEdit);
                quantityEdit.addTextChangedListener(new TextWatcher(){

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //int newQuantity = Integer.parseInt(s.toString());

                        int newQuantity;
                        if(s.toString() == null || s.toString() == "" || s.toString().isEmpty()){
                            newQuantity = 1;
                        }
                        else{
                            newQuantity = Integer.parseInt(s.toString());
                        }
                        editedProduct.setDisplayedPrice(editedProduct.getPrice() * (newQuantity));
                        editedProduct.setQuantity(newQuantity);
                        priceEdit.setText(editedProduct.getDisplayedPrice() + "");
                        if(editedProduct.getLinkedProduct() != null){
                            editedProduct.getLinkedProduct().setQuantity(editedProduct.getQuantity());
                            editedProduct.getLinkedProduct().setDisplayedPrice(editedProduct.getLinkedProduct().getPrice() * editedProduct.getLinkedProduct().getQuantity());
                            //deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                            deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                        }
                        adapter.notifyDataSetChanged();

                    }
                });

                ImageButton plusButtonEdit = row.findViewById(R.id.plusButtonEdit);
                ImageButton minusButtonEdit = row.findViewById(R.id.minusButtonEdit);
                plusButtonEdit.setOnClickListener((view) ->{

                    if(quantityEdit.getText().toString().trim().length() == 0){
                        quantityEdit.setText(1 + "");
                    }
                    int convertedToInt = Integer.parseInt(quantityEdit.getText().toString());
                    editedProduct.setDisplayedPrice(editedProduct.getPrice() * (convertedToInt + 1));
                    editedProduct.setQuantity(convertedToInt + 1);
                    quantityEdit.setText(editedProduct.getQuantity() + "");
                    if(editedProduct.getLinkedProduct() != null){
                        editedProduct.getLinkedProduct().setQuantity(editedProduct.getQuantity());
                        editedProduct.getLinkedProduct().setDisplayedPrice(editedProduct.getLinkedProduct().getPrice() * editedProduct.getLinkedProduct().getQuantity());
                        deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                    }
                    adapter.notifyDataSetChanged();
                });

                minusButtonEdit.setOnClickListener((view) -> {
                    if(editedProduct.getQuantity() > 1 && quantityEdit.getText().toString().trim().length() > 0) {
                        int convertedToInt = Integer.parseInt(quantityEdit.getText().toString());
                        editedProduct.setDisplayedPrice(editedProduct.getPrice() * (convertedToInt - 1));
                        editedProduct.setQuantity(convertedToInt - 1);
                        quantityEdit.setText(editedProduct.getQuantity() + "");
                        if(editedProduct.getLinkedProduct() != null){
                            editedProduct.getLinkedProduct().setQuantity(editedProduct.getQuantity());
                            editedProduct.getLinkedProduct().setDisplayedPrice(editedProduct.getLinkedProduct().getPrice() * editedProduct.getLinkedProduct().getQuantity());
                            deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setView(row);
                //alertDialog.setView(testView);
                alertDialog.create().show(); */
            });

            link.setOnClickListener((v) -> {

                 /*Intent intent = new Intent(CartActivity.this, EmptyActivity.class);
                intent.putExtras(dataToPass);
                startActivity(intent); // may need to be changed */
                Intent intent = new Intent(DigitalReceipt.this, Link.class);
                //intent.putExtras(dataToPass);
                intent.putExtra("Index", position);
                setProductToPass(product);
                setIndex(position);
                Link.setPassedContext(DigitalReceipt.this);
                startActivity(intent); // may need to be changed
            });
            return convertView;
        }
    }
}