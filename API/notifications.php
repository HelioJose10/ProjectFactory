<?php
// Conectar ao banco de dados
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

if (!isset($_GET['id'])) {
    http_response_code(400);
    echo json_encode(['error' => 'Missing parameter: id']);
    exit;
}

$id = $_GET['id'];

$stmt = $mysqli->prepare("SELECT d.valor, DATE_FORMAT(d.hora, '%d/%m/%Y') as data, DATE_FORMAT(d.hora, '%H:%i') as hora 
                          FROM distancia d
                          INNER JOIN utilizadores u ON d.id_utilizador = u.id
                          WHERE d.valor > 0 AND u.id = ?");
$stmt->bind_param("i", $id);
$stmt->execute();

$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $data = array();

    while ($row = $result->fetch_assoc()) {
        $data[] = $row;
    }

    echo json_encode($data, JSON_UNESCAPED_UNICODE);
} else {
    http_response_code(404);
    echo json_encode(['error' => 'No data found']);
}

$stmt->close();
$mysqli->close();
?>
