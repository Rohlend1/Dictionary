import React, { useState,useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Modal from './Modal';
import Logout from './Logout';
import Delete from './Delete';

const UserSettings = () => {
    const link = process.env.REACT_APP_LINK
    let navigate = useNavigate()
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [showStateL,setShowStateL] = useState(false)
    const [showStateD,setShowStateD] = useState(false)
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
        if (username !== user.username) {
          try {
            const response = await axios.patch(
              `${link}/rename`,null,{params: {new_name: username},
                headers: {
                  'Authorization': Authorization
                },
              }
            );
            localStorage.setItem("jwt", response.data.jwt);
            window.location.reload();
          } catch (err) {
            console.error('Ошибка name change:', err);
            if (err.code === "ERR_BAD_REQUEST"){
                alert("Такой аккаунт уже существует")
            } else {
                alert("Server unavailable")
            }
          }
        }
      };

        const handlePassChange = async () => {  
            if(password){
            axios.patch(`${link}/change/pass`,{password:password},{
                    headers:{
                        'Authorization':Authorization   
                    }
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
            <Modal active={showStateL} setActive={setShowStateL} children={<Logout/>}/>
            <Modal active={showStateD} setActive={setShowStateD} children={<Delete/>}/>
            <div className='font-title'>Настройки пользователя</div>
                <div className="settings-group">
                    <input type="text" placeholder="Имя пользователя" value={username} onChange={(e)=>{setUsername(e.target.value)}} />
                </div>
                <button className='button' onClick={()=>{handleNameChange()}}>Изменить имя</button>
                <div className="settings-group">
                    <input type="password" placeholder="Пароль" value={password} onChange={(e)=>{setPassword(e.target.value)}} />
                </div>
                <button className='button' onClick={()=>{handlePassChange()}}>Изменить пароль</button>
                <div className="settings-group">
                    <div className='deletion-alert'>
                        Удаление аккаунта
                    </div>
                </div>
                <button className='button button-alert' onClick={()=>{setShowStateD(true)}}>Delete</button>
        </div>
    );
};

export default UserSettings;
