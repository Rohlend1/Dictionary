import React, { useState,useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const UserSettings = () => {
    const link = "http://localhost:8080"
    let navigate = useNavigate()
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [user,setUser] = useState('')
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`

    const handleLogout = async () => {
        try {
            const response = await axios.post(`${link}/logout`,{headers:{
            'Authorization':Authorization
        }
        }); 
         localStorage.clear()
            navigate("/login")
            console.log(response.data)
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        }
    }
    

    const fetchUser = async () => {
        try {
            const response = await axios.get(`${link}/me`,{headers:{
            'Authorization':Authorization
        }
        }); 
            setUser(response.data);
            setUsername(response.data.username)
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        }
    };

    const handleNameChange = async () => {  
        console.log(username)
        if (username !== user.username){
        axios.patch(`${link}/rename`,{new_name:username},{headers:{
                    'Authorization':Authorization
                },
            }
            ).then(response => {
                localStorage.setItem("jwt",response.data.jwt)
                window.location.reload()
            }).catch(err => console.error('Ошибка name change:', err))
        }};

        const handlePassChange = async () => {  
            if(password){
            axios.patch(`${link}/repass`,{new_password:password},{headers:{
                        'Authorization':Authorization
                    },
                }
                ).then(response => {
                    console.log(response.data)
                    handleLogout()
                    navigate("/login")
                }).catch(err => console.error('Ошибка:', err));
            }
            };

    useEffect(()=>{
        fetchUser()    
    },[])
    return (
        <div className="settings-container">
            <div className='font-title'>Настройки пользователя</div>
                <div className="settings-group">
                    <input type="text" placeholder="Имя пользователя" value={username} onChange={(e)=>{setUsername(e.target.value)}} />
                </div>
                <button className='button' onClick={()=>{handleNameChange()}}>Изменить имя</button>
                <div className="settings-group">
                    <input type="password" placeholder="Пароль" value={password} onChange={(e)=>{setPassword(e.target.value)}} />
                </div>
                <button className='button' onClick={()=>{handlePassChange()}}>Изменить пароль</button>
        </div>
    );
};

export default UserSettings;
