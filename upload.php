<?php
$UPLOAD_DIR = "/home/cookie/videos/";

if(isset($_FILES['demo'])){
    $tmp=explode(".",$_FILES['demo']['name']);
    $suffix_name = end($tmp);
    $prefix_name = microtime();
    $name = $prefix_name.".".$suffix_name;
    $name = $_FILES['demo']['name'];
    $path = $UPLOAD_DIR.$name;
    move_uploaded_file($_FILES['demo']['tmp_name'],$path);
    echo $prefix_name;
}else{
    header('HTTP/1.1 403 Forbidden');
}
?>