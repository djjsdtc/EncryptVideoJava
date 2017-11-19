<?php
$ENC_PATH = "/home/cookie/encryptvideo.jar";
$JAVA_COMMAND = "java -jar ".$ENC_PATH;
$OUTPUT_DIR = "/home/cookie/nginx/videos/";
$INPUT_DIR = "/home/cookie/videos/";
$PROP_PATH = "/home/cookie/encvideo.properties";

if(isset($_REQUEST['input']) && isset($_REQUEST['id'])){
	$input = $INPUT_DIR.$_REQUEST['input'].".mp4";
	$output = $OUTPUT_DIR.$_REQUEST['input'].".m3u8";
	$id = $_REQUEST['id'];
	$command = $JAVA_COMMAND." -i ".$input." -n ".$id." -o ".$output." -p ".$PROP_PATH;
	$command = $command." >/dev/null 2&>1";
	exec($command);
	echo('success');
}else{
	echo('error');
}
?>
