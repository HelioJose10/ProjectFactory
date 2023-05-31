<?php
if(isset($_GET['id'])) {
    $id = $_GET['id'];
} else {
    http_response_code(400);
    echo json_encode(['error' => 'Parameter \'id\' is missing']);
    exit;
}

$host = 'localhost';
$db   = 'rubenpas_techelmt';
$user = 'rubenpas_sp53';
$pass = 'Password0524*';
$charset = 'utf8mb4';

$mysqli = new mysqli($host, $user, $pass, $db);
if ($mysqli->connect_error) {
    http_response_code(500);
    echo json_encode(['error' => 'Failed to connect to the database']);
    exit;
}

$mysqli->set_charset($charset);

$stmt = $mysqli->prepare("SELECT Nome, Email, Telemovel FROM utilizadores WHERE id = ?");
$stmt->bind_param("i", $id);
$stmt->execute();

$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $data = $result->fetch_assoc();
    echo json_encode($data, JSON_UNESCAPED_UNICODE);
} else {
    http_response_code(404);
    echo json_encode(['error' => 'No data found']);
}

$stmt->close();
$mysqli->close();
?>