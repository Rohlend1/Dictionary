import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import Logout from './Logout';
import Modal from './Modal';
const Navbar = () => {
    const link = "http://localhost:8080"
    const navigate = useNavigate()
    const [user, setUser] = useState('Unknown');
    const [showStateS,setShowStateS] = useState(false)
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`
    const fetchDictionaries = async () => {
        try {
            const response = await axios.get(`${link}/me`,{headers:{
            'Authorization':Authorization
        }
        }); 
            setUser(response.data);
            console.log(user)
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        }
    };
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
    
    useEffect(() => {
        fetchDictionaries();
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    return (
            <div className="navbar">
            {user.username !== undefined ? (
                 <div className="user">
                <div>{user.username}</div>
                <div>{user.createdAt}</div>
                <button className='button' onClick={()=>{setShowStateS(true)}}>Logout</button>
                </div>
            ) : (
               <button className='button' onClick={()=>navigate("/login")}>Log In</button>
            )}
             <Modal active={showStateS} setActive={setShowStateS} children={<Logout/>}/>
        </div>
        
    );
};

export default Navbar;
