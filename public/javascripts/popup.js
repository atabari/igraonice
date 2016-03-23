/**
 * Created by User on 2/24/2016.
 */
var apId = $('#apId').val();

var obavijest = "Trenutno nema zauzetih termina";
var time;
function pkgDuration(duration) {
    time = duration;
}

$('#checkIn').change(function() {
    var date = $('#checkIn').val();
    var dateApId = date +"-"+ apId

    $.ajax({
        data:dateApId,
        type: "GET",
        url: "/ajax/" + dateApId
    }).success(function(response) {
        if(response == 'null') {
            $('#datumi').text(obavijest)
        } else {
            $('#datumi').text(response)
        }
    });
});

//var time = $('.time').val();
var timeFromCheck = $('#timeFromCheck').val();
var timeToCheck = $('#timeToCheck').val();

$('#timeFrom').change(function() {
    var timeFrom = $('#timeFrom').val();
    var timeTo = (1*timeFrom) + (1*time);
    $('#timeTo').val(timeTo)

    if(timeFrom < timeFromCheck && timeTo > timeToCheck){
        $('#time_error').text("Odabrani termini nisu u okviru radnog vremena!")
    }else{
        $('#time_error').text(" ");
    }
});