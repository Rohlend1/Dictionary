import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import Loader from './Loader'

const Login = () => {
    const link = "http://localhost:8080"
    const navigate = useNavigate()
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isLoading,setIsLoading] = useState(false)

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setIsLoading(true)
        try {
            const response = await axios.post(
                `${link}/auth/login`, 
                { username, password }
                );
                localStorage.setItem("jwt",response.data.jwt)
                setIsLoading(false)
                navigate('/profile')
        } catch (error) {
            console.error('Ошибка при авторизации:', error);
            setIsLoading(false)
            if (error.code === "ERR_BAD_REQUEST"){
                alert("Неправильный логин или пароль")
            }
        }
    };
    
    if (isLoading) return <Loader />

    return (
        <div className="login-container">
            <div className='login-form shadow'>
            <div className="form-group">
                    <div className='top-title' style={{fontWeight:"600"}}>Авторизация</div>
                    </div>
                    <input type="text" placeholder="Имя пользователя" value={username} onChange={handleUsernameChange} />
                    <input type="password" placeholder="Пароль" value={password} onChange={handlePasswordChange} />
                <button className='button' onClick={handleSubmit}>Войти</button>
            </div>
            <div style={{marginTop:"20px"}}>
            <div style={{fontSize:"15px",opacity:"0.8",marginBottom:"10px",textAlign:"center"}}>Нет asd?</div>
                <button className='button button-login'  onClick={()=>navigate("/register")}>Зарегистрироваться</button>
                </div>
        </div>
    );
};

export default Login;
