var firebaseRef = new Firebase("https://shoppinglistbaaa.firebaseio.com/");
var itemsRef = new Firebase("https://shoppinglistbaaa.firebaseio.com/items");
var productList = document.getElementById("productsList");

//when the user presses the "Add" button
document.getElementById("addProduct").addEventListener("click", function(){
  //take input from user
  var product = document.getElementById("productName").value;
  var amount = document.getElementById("productAmount").value;
  
  //save lists of data with the help of child and push(); reference https://www.firebase.com/docs/web/guide/saving-data.html
  var productsRef = firebaseRef.child("items");
  productsRef.push({
    product: product,
    amount: amount
  });
});

//take data from the db node specified and display it to the user; in this case there's only items to be displayed so that's the node
itemsRef.on("child_added", function(snapshot){
  var newItem = snapshot.val();
  //get all the products to be displayed to the user; references for creating list elements and checkboxes in js:  http://www.w3schools.com/js/js_htmldom_nodes.asp and http://www.w3schools.com/jsref/dom_obj_checkbox.asp; not sure that this is the best way to do it, because it looks too long
  var listElement = document.createElement("li");
  var checkboxElement = document.createElement("INPUT");
  checkboxElement.setAttribute("type", "checkbox");
  var listTextNode = document.createTextNode(" " + newItem.amount + " " + newItem.product);
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
    itemsRef.remove();
    productList.removeChild(listElement);
  });
});

//update the data retrieved from the database after changes occur to the initial data display
itemsRef.on("child_changed", function(snapshot){
  var changedItem = snapshot.val();
  var listTextNode = document.createTextNode(" " + changedItem.amount + " " + changedItem.product);
  listElement.appendChild(listTextNode);
  productList.appendChild(listElement);
});

//update the data initially retrieved after it's been removed
itemsRef.on("child_removed", function(snapshot){
  var removedItem = snapshot.val();
  var listTextNode = document.createTextNode(" " + changedItem.amount + " " + changedItem.product);
  listElement.appendChild(listTextNode);
  productList.removeChild(listElement);
});
