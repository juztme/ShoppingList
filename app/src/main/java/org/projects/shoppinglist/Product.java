package org.projects.shoppinglist;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by juztm_000 on 14-Mar-16.
 */
public class Product implements Parcelable{

  private String name;
  private int quantity;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeInt(quantity);
  }

  //creator
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
    public Product createFromParcel(Parcel in){
      return new Product(in);
    }

    @Override
    public Object[] newArray(int size) {
      return new Object[0];
    }
  };

  public Product(String name, int quantity){
    this.name = name;
    this.quantity = quantity;
  }

  //de-parcel object
  public Product(Parcel in) {
    name = in.readString();
    quantity = in.readInt();
  }

  @Override
  public String toString(){
    return name + " " + quantity;
  }

}
