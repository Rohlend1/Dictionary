import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";

const Login = () => {
    const link = "http://localhost:8080"
    const navigate = useNavigate()
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post(
                `${link}/auth/login`, 
                { username, password }
                );
                navigate('/profile')
                localStorage.setItem("jwt",response.data.jwt)
            console.log(response.data);
        } catch (error) {
            console.error('Ошибка при авторизации:', error);
        }
    };

    return (
        <div className="login-container">
            <h1>Авторизация</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <input type="text" placeholder="Имя пользователя" value={username} onChange={handleUsernameChange} />
                </div>
                <div className="form-group">
                    <input type="password" placeholder="Пароль" value={password} onChange={handlePasswordChange} />
                </div>
                <button className='button' type="submit">Войти</button>
            </form>
        </div>
    );
};

export default Login;
