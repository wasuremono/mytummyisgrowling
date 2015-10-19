<?php
 
class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * return new user
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $encrypted_password = password_hash($password,PASSWORD_DEFAULT); // encrypted password 
        $q = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, created_at) VALUES(?, ?, ?, ?, NOW())");
        $q->bind_param("ssss", $uuid, $name, $email, $encrypted_password);
        $result = $q->execute();
        $q->close();
 
        // check for successful store
        if ($result) {
            $q = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $q->bind_param("s", $email);
            $q->execute();
            $user = $q->get_result()->fetch_assoc();
            $q->close();
 
            return $user;
        } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function verifyUser($email, $password) {
 
        $q = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
 
        $q->bind_param("s", $email);
 
        if ($q->execute()) {
            $user = $q->get_result()->fetch_assoc();
            $q->close();
            //Return the user information if email and password are both correct
            if(password_verify($password,$user["encrypted_password"])){
            
                return $user;
            }else {
                return NULL;
            }
        } else {
            return NULL;
        }
    }
 
    /**
     * Check if user exists
     */
    public function userExists($email) {
        $q = $this->conn->prepare("SELECT email from users WHERE email = ?");
 
        $q->bind_param("s", $email);
 
        $q->execute();
 
        $q->store_result();
 
        if ($q->num_rows > 0) {
            // user exists
            $q->close();
            return true;
        } else {
            // user does not exist
            $q->close();
            return false;
        }
    }
 
    public function changePrefs($id,$cuisine,$rank){
        $q = $this->conn->prepare("SELECT * FROM userprefs WHERE (user = ? AND rank = ?)");
        $q->bind_param("ss", $id,$rank);
        $q->execute();
        $q->store_result();
        if($q->num_rows > 0){
            $q = $this->conn->prepare("UPDATE userprefs SET cuisine = ? where (user = ? AND rank = ?)");
            $q->bind_param("sss", $cuisine,$id, $rank);
            $q->execute();
            $q->close();
            return true;
        }else {
            $q = $this->conn->prepare("INSERT INTO userprefs(user,cuisine,rank) VALUES(?,?,?)");
            $q->bind_param("sss",$id,$cuisine,$rank);
            $q->execute();
            $q->close();
            return true;
        }
        return true;
    
    }
    
}
 
?>
