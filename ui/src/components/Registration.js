import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";

const Registration = () => {
    const link = "https://8080-rohlend1-dictionary-5jnb5hheiop.ws-eu99.gitpod.io"
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
                // console.log(response.data);
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
            <h1>Регистрация</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <input type="text" placeholder="Имя пользователя" value={username} onChange={handleUsername} />
                </div>
                <div className="form-group">
                    <input type="password" placeholder="Пароль" value={password} onChange={handlePassword} />
                </div>
                <div className="form-group">
                    <input type="password" id="pass2" placeholder="Повторите пароль" value={password2} onChange={handlePassword2} />
                </div>
                <button className='button' type="submit" >
                    Регистрация
                </button>
                
            </form>
        </div>
    );
};

export default Registration;