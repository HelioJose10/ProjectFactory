<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");

// Conectar ao banco de dados
$host = 'localhost';
$db   = 'rubenpas_techelmt';
$user = 'rubenpas_sp53'; // substitua com seu username do MySQL
$pass = 'Password0524*'; // substitua com sua password do MySQL
$charset = 'utf8mb4';

$mysqli = new mysqli($host, $user, $pass, $db);
if ($mysqli->connect_error) {
    http_response_code(500); // Internal server error
    echo json_encode(['error' => 'Failed to connect to the database']);
    exit;
}

// Preparar e executar a consulta
$stmt = $mysqli->prepare("SELECT valor, DATE_FORMAT(hora, '%d/%m/%Y') as data, DATE_FORMAT(hora, '%H:%i') as hora FROM distancia WHERE valor > 0");
$stmt->execute();

// Obter o resultado
$result = $stmt->get_result();

// Criar um array para armazenar todos os resultados
$data = array();

while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

if (count($data) > 0) {
    // Retornar os dados como JSON
    echo json_encode($data, JSON_UNESCAPED_SLASHES);
} else {
    http_response_code(404); // Not found
    echo json_encode(['error' => 'No data found']);
}

$stmt->close();
$mysqli->close();

?>
