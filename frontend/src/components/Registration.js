import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";

const Registration = () => {
    const link = process.env.REACT_APP_LINK
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [password2, setPassword2] = useState('');
    const [checkStatus, setCheckStatus] = useState(false)
    const handleUsername = (event) => {
        setUsername(event.target.value);
    };

    const handlePassword = (event) => {
        const { value } = event.target;
        setPassword(value);
        checkPassword(value, password2);
    };

    const handlePassword2 = (event) => {
        const { value } = event.target;
        setPassword2(value);
        checkPassword(password, value);
    };

    const checkPassword = (pass1, pass2) => {
        if (pass1 === pass2) {
            setCheckStatus(true);
        } else {
            setCheckStatus(false);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (checkStatus) {
            try {
                const response = await axios.post(`${link}/auth/registration`,{ username, password}
                );
                if (response.data){
                navigate('/profile')
                localStorage.setItem("jwt",response.data.jwt)
                }
            } catch (error) {
                console.error('Ошибка при авторизации:', error);
            }
        } else{
            alert('Пароли не совпадают');
        }
    };

    return (
        <div className="login-container">
            <div className="login-form shadow">
                <div className="form-group">
                    <div className='top-title' style={{fontWeight:"600"}}>Регистрация</div>
                </div>
                    <input type="text" placeholder="Имя пользователя" value={username} onChange={handleUsername} />
                    <input type="password" placeholder="Пароль" value={password} onChange={handlePassword} />
                    <input type="password" id="pass2" placeholder="Повторите пароль" value={password2} onChange={handlePassword2} />
                <button className='button' type="submit" onClick={handleSubmit}>Регистрация</button>
            </div>
            <div style={{marginTop:"20px"}}>
            <div style={{fontSize:"15px",opacity:"0.8",marginBottom:"10px",textAlign:"center"}}>Уже есть аккаунт?</div>
                <button className='button button-login' onClick={()=>navigate("/login")}>Войти</button>
                </div>
        </div>
    );
};

export default Registration;