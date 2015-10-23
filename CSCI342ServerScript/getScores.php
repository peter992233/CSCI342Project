<?php

header('Content-type: application/json');

$link = mysql_connect('localhost', 'root', 'csci321kw1');
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

$query = "SELECT * FROM highscore ORDER BY score DESC";

$result = mysql_query($query);

if (!$result) {
    echo json_encode("error");
    mysql_close($link);
    exit();
}
$data = array();

while ($row = mysql_fetch_assoc($result)) {
    array_push($data, $row);
}

echo json_encode($data);

mysql_free_result($result);

mysql_close($link);
?>
