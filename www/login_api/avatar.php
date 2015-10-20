<?php
 header("Content-Type: image/png");
 $filename = 'avatars/'.$_POST["user"].'.png';
 return file_put_contents($filename,base64_decode($_POST["data"]));
 

?>

