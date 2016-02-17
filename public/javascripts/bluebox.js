/**
 * Created by User on 1/6/2016.
 */
Date.prototype.toDateInputValue = (function() {
    var local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0,10);
});

document.getElementById('checkIn').value = new Date().toDateInputValue();
document.getElementById('checkOut').value = new Date().toDateInputValue();


function totalNights(price){
    var checkin = new Date(document.getElementById('checkIn').value);
    var checkout = new Date(document.getElementById('checkOut').value);
    var timeDiff = Math.abs(checkout.getTime() - checkin.getTime());
    var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

    $('#total').text(diffDays);



    var money = diffDays *  price;

    $('#totalmoney').text(money);
}
function capacity(){
    var person = document.getElementById("person").value;
    $('#nopersons').text(person);
}
