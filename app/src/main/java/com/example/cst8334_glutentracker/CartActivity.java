package com.example.cst8334_glutentracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.Product;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private Adapter adapter = new Adapter();
    //private ArrayList<Product> productsArrayList = new ArrayList<Product>();
    private static ArrayList<Product> productsArrayList = new ArrayList<Product>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ListView purchases = findViewById(R.id.purchases);
        Button addNewProductButton = findViewById(R.id.addNewProductButton);
        addNewProductButton.setOnClickListener((v) -> {
            startActivity(new Intent(CartActivity.this, ScanActivity.class));
        });
        Double.valueOf("5");
        //productsArrayList.add(new Product(1, "Oreo", "Milk's favorite cookie", "test", 3.00, false));
        //productsArrayList.add(new Product(2, "Gluten Free Cookie", "A gluten free cookie", "test", 5.00, true));
        purchases.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        addProductButton.setOnClickListener((View v)->{
//
//        });

       /* if(productsArrayList.size() > 0){
            Button removeButton = findViewById(R.id.removeFromCart);
            removeButton.setOnClickListener((v) ->{
                int position = purchases.getPositionForView(v);
                productsArrayList.remove(position);
                Toast.makeText(this, "Should be working", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            });
        } */

    }

    public static ArrayList<Product> getProductsArrayList(){
        return productsArrayList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getProductsArrayList().get(1).setPrice(9.00);
        adapter.notifyDataSetChanged();
    }

    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return productsArrayList.size();
        }

        @Override
        public Product getItem(int position) {
            return productsArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return productsArrayList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Product product = (Product) getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View newView;
            if(product.getLinkedProduct() == null){
                newView = inflater.inflate(R.layout.activity_product_list, parent, false);
            }
            else{
                newView = inflater.inflate(R.layout.activity_product_list_linked, parent, false);
                /*TextView deductibleText = newView.findViewById(R.id.deductibleText);
                deductibleText.setText((product.getPrice() - product.getLinkedProduct().getPrice()) + ""); */
            }
           // View newView = inflater.inflate(R.layout.activity_product_list, parent, false);   worked
            TextView deductibleText = newView.findViewById(R.id.deductibleText);
            if(product.getLinkedProduct() != null) {
                deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + ""); //changed to displayed price
            }
            TextView productName = newView.findViewById(R.id.productName);
            productName.setText(product.getProductName());
            //TextView price = newView.findViewById(R.id.price);
            EditText price = newView.findViewById(R.id.price);
            price.setText(product.getDisplayedPrice() + "");
            EditText quantity = newView.findViewById(R.id.quantity);
            //quantity.setText("1");
            quantity.setText(Integer.toString(product.getQuantity()));

            Button plusButton = newView.findViewById(R.id.plusButton);
            plusButton.setOnClickListener((v) ->{
               /* String currentQuantity = quantity.getText().toString();
                int converted = Integer.parseInt(currentQuantity);
                converted = converted + 1;
                quantity.setText(Integer.toString(converted));
                double multiPrice = Double.parseDouble(price.getText().toString());
                multiPrice = multiPrice * converted;
                price.setText(Double.toString(multiPrice));
                adapter.notifyDataSetChanged(); */
                int convertedToInt = Integer.parseInt(quantity.getText().toString());
                product.setDisplayedPrice(product.getPrice() * (convertedToInt + 1));
                product.setQuantity(convertedToInt + 1);
                if(product.getLinkedProduct() != null){
                    product.getLinkedProduct().setQuantity(product.getQuantity());
                    product.getLinkedProduct().setDisplayedPrice(product.getLinkedProduct().getPrice() * product.getLinkedProduct().getQuantity());
                    deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                }
                adapter.notifyDataSetChanged();
            });

            Button minusButton = newView.findViewById(R.id.minusButton);
            minusButton.setOnClickListener((v) ->{
                if(product.getQuantity() > 1) {
                    int convertedToInt = Integer.parseInt(quantity.getText().toString());
                    product.setDisplayedPrice(product.getPrice() * (convertedToInt - 1));
                    product.setQuantity(convertedToInt - 1);
                    if(product.getLinkedProduct() != null){
                        product.getLinkedProduct().setQuantity(product.getQuantity());
                        product.getLinkedProduct().setDisplayedPrice(product.getLinkedProduct().getPrice() * product.getLinkedProduct().getQuantity());
                        deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                    }
                    adapter.notifyDataSetChanged();
                }
            });

           Button removeButton = newView.findViewById(R.id.removeFromCart);
            removeButton.setOnClickListener((v) -> {
                productsArrayList.remove(position);
                //Toast.makeText(this, "Should be working", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            });

            Button linkButton = newView.findViewById(R.id.linkButton);
            linkButton.setOnClickListener((v) -> {
                // Finish the rest of this
                Bundle dataToPass = new Bundle();
                //dataToPass.putSerializable("Product", product);
                dataToPass.putInt("Index", position);

               /* Intent intent = new Intent(CartActivity.this, EmptyActivity.class);
                intent.putExtras(dataToPass);
                startActivity(intent); // may need to be changed */
                 Intent intent = new Intent(CartActivity.this, Link.class);
                //intent.putExtras(dataToPass);
                intent.putExtra("Index", position);
                startActivity(intent); // may need to be changed
            });

            return newView;
            //return null;
        }
    }


}