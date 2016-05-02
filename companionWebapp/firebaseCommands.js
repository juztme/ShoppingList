var firebaseRef = new Firebase("https://shoppinglistbaaa.firebaseio.com/");
var productList = document.getElementById("productsList");
var productsRef = firebaseRef.child("items");


//when the user presses the "Add" button
document.getElementById("addProduct").addEventListener("click", function(){
  //take input from user
  var name = document.getElementById("productName").value;
  var quantity = parseInt(document.getElementById("productAmount").value);
  //in the Android app, there's an extra property for the drop-down list for the amount in the Product class, but there's no point in having it in the JS web app; setting listQuantity to "0" should be enough for the Android app to not crash while trying to read the data from Firebase
  var listQuantity = "0";
  
  //save lists of data with the help of child and push(); reference https://www.firebase.com/docs/web/guide/saving-data.html
  productsRef.push({
    name: name,
    quantity: quantity,
    listQuantity: listQuantity
  });
});

//take data from the db node specified and display it to the user; in this case there's only items to be displayed so that's the node
productsRef.on("child_added", function(snapshot){
  var newItem = snapshot.val();
  //get all the products to be displayed to the user; references for creating list elements and checkboxes in js:  http://www.w3schools.com/js/js_htmldom_nodes.asp and http://www.w3schools.com/jsref/dom_obj_checkbox.asp; not sure that this is the best way to do it because it looks too long, but it works
  var listElement = document.createElement("li");
  var checkboxElement = document.createElement("INPUT");
  checkboxElement.setAttribute("type", "checkbox");
  var listTextNode = document.createTextNode(" " + newItem.quantity + " " + newItem.name);
  listElement.appendChild(checkboxElement);
  listElement.appendChild(listTextNode);
  productList.appendChild(listElement);
  
  document.getElementById("deleteProduct").addEventListener("click", function(){
    //remove only the checked elements from the database when the user clicks on the delete button
      if(checkboxElement.checked){
        snapshot.ref().remove();
        //also update the page when the elements are removed
        productList.removeChild(listElement);
      }
    });
  
  document.getElementById("clearProducts").addEventListener("click", function(){
    //delete everything from the items node
    productsRef.remove();
    productList.removeChild(listElement);
  });
});


