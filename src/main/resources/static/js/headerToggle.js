$(document).ready(function(){
   $("#right_menu").hide();
   $("#left_menu").hide();
   $("#notification_menu").hide();

   $("#myPageButton").click(function(){
      $("#left_menu").hide();
      $("#notification_menu").hide();
      $("#right_menu").toggle();
   });

   $("#notificationButton").click(function(){
      $("#right_menu").hide();
      $("#left_menu").hide();
      $("#notification_menu").toggle();
   });

   $("#categoryButton").click(function(){
      $("#right_menu").hide();
      $("#notification_menu").hide();
      $("#left_menu").toggle();
   });
});