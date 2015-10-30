<?php

header('Content-type: application/json');

if (isset($_POST["uName"])) {
    $uName = $_POST["uName"];
} else {
    echo json_encode("error");
    exit();
}

if (isset($_POST["score"])) {
    $score = $_POST["score"];
} else {
    echo json_encode("error");
    exit();
}

$link = mysql_connect('localhost:3306', 'root', 'csci321kw1');
if (!$link) {
    echo json_encode("error");
    exit();
}

$db_selected = mysql_select_db('csci342game', $link);
if (!$db_selected) {
    echo json_encode("error");
    mysql_close($link);
    exit();
}

$query = sprintf("INSERT INTO highscore (uName, score)
VALUES ('%s', '%s')",
    mysql_real_escape_string($uName),
    mysql_real_escape_string($score));

$result = mysql_query($query);

if (!$result) {
    echo json_encode("error");
    mysql_close($link);
    exit();
}

mysql_free_result($result);

mysql_close($link);

echo json_encode("OK");

?>
