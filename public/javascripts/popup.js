/**
 * Created by User on 2/24/2016.
 */
var apId = $('#apId').val();

var obavijest = "Trenutno nema zauzetih termina";

$('#checkIn').change(function(){
    var date = $('#checkIn').val();
    var dateApId = date +"-"+ apId

    $.ajax({
        data:dateApId,
        type: "GET",
        url: "/ajax/" + dateApId
    }).success(function(response) {
        if(response != null){
            $('#datumi').text(response)
        }else{
            $('#datumi').text(obavijest)
        }

    });

});
var time = $('#time').val();

$('#datetimepicker2').change(function(){
    var timeFrom = $('#datetimepicker2').val();
    var timeTo = (1*timeFrom) + (1*time);

    $('#timeTo').val(timeTo)

});