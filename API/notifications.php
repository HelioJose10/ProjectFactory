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

// Verificar se o parâmetro "id" foi fornecido na URL
if (!isset($_GET['id'])) {
    http_response_code(400); // Bad Request
    echo json_encode(['error' => 'Missing parameter: id']);
    exit;
}

$id = $_GET['id'];

// Preparar e executar a consulta com o parâmetro "id"
$stmt = $mysqli->prepare("SELECT d.valor, DATE_FORMAT(d.hora, '%d/%m/%Y') as data, DATE_FORMAT(d.hora, '%H:%i') as hora 
                          FROM distancia d
                          INNER JOIN utilizadores u ON d.id_utilizador = u.id
                          WHERE d.valor > 0 AND u.id = ?");
$stmt->bind_param("i", $id);
$stmt->execute();

// Obter o resultado
$result = $stmt->get_result();

// Verificar se foram encontrados resultados
if ($result->num_rows > 0) {
    // Criar um array para armazenar os dados
    $data = array();

    // Ler os dados do resultado da consulta
    while ($row = $result->fetch_assoc()) {
        $data[] = $row;
    }

    // Retornar os dados como JSON
    echo json_encode($data, JSON_UNESCAPED_UNICODE);
} else {
    http_response_code(404); // Not found
    echo json_encode(['error' => 'No data found']);
}

$stmt->close();
$mysqli->close();

?>
