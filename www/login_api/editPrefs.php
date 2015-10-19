<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
// json response array
$json_decoded = json_decode($_POST['JSON'],true);
$numUpdates = 0;
foreach($json_decoded as &$pair){
$cuisine = (string)$pair['cuisine'];
$rank = (integer)$pair['rank'];
$db->changePrefs($_POST['id'],$cuisine,$rank);
$numUpdates++;
}
while($numUpdates < 11){
$numUpdates++;
$db->changePrefs($_POST['id'],NULL,$numUpdates);
}
?>

