<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Scanner</title>
</head>
<body>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
    <input type="text" id="hasil">
    <button id="scanner" onclick="scan()">scan</button>
</body>
<script>
// memanggil method Android bernama scanBarcode : pada JavascriptInterface
function scan(){
    // var send = document.getElementById('hasil').value;
    Android.scanBarcode();
}

// Android memanggil function Javascript, untuk set input value
function FromAndroidToWebview(hasilScan){
    document.getElementById('hasil').value = hasilScan;
}
</script>
</html>