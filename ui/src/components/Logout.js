import React, { useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
const Logout = () => {
    const link = "http://localhost:8080"
    const navigate = useNavigate()
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
    
    useEffect(() => {
    }, []);
    return (
            <div className="logout">
                <div className='logout-title'>Are you sure?</div>
                <button className='button' onClick={()=>{handleLogout()}}>Logout</button>
                </div>
        
    );
};

export default Logout;
