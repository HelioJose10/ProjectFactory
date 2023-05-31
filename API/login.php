<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");

if (!isset($_GET['email']) || !isset($_GET['password'])) {
    http_response_code(400);
    echo json_encode(['error' => 'Missing email or password']);
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

$stmt = $mysqli->prepare("SELECT id FROM utilizadores WHERE Email = ? AND Password = ?");
$stmt->bind_param("ss", $_GET['email'], $_GET['password']);
$stmt->execute();

$result = $stmt->get_result();
$user = $result->fetch_assoc();

if ($user) {
    echo json_encode(['id' => $user['id']]);
} else {
    http_response_code(404);
    echo json_encode(['error' => 'Invalid email or password']);
}

$stmt->close();
$mysqli->close();

?>
