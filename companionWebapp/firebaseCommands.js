var firebaseRef = new Firebase("https://shoppinglistbaaa.firebaseio.com/");
var productList = document.getElementById("productsList");
var productsRef = firebaseRef.child("items");

//when the user presses the "Add" button
document.getElementById("addProduct").addEventListener("click", function(){
  //take input from user
  var name = document.getElementById("productName").value;
  var quantity = parseInt(document.getElementById("productAmount").value);
  //listQuantity should still have a value, otherwise 0 will be displayed to the user and that's not nice; so listQuantity should have the same value as the quantity field 
  var listQuantity = String(quantity);
  
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
  if(newItem.quantity != 0){
    var listTextNode = document.createTextNode(" " + newItem.quantity + " " + newItem.name);
  } else {
    var listTextNode = document.createTextNode(" " + newItem.listQuantity + " " + newItem.name);
  }
  
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


