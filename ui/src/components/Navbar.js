import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import Logout from './Logout';
import Modal from './Modal';
import UserSettings from './UserSettings';
const Navbar = (user,setUser) => {
    const navigate = useNavigate()
    // const [user, setUser] = useState('Unknown');
    const [showStateL,setShowStateL] = useState(false)
    const [showStateS,setShowStateS] = useState(false)
    const [showStateU,setShowStateU] = useState(false)
    useEffect(()=>{
        console.log(showStateU)
    },[showStateU])
    return (
            <div className="navbar">
            {user.user !== undefined ? (
                 <div className="user">
                <div className='user-contents' onMouseEnter={()=>{setShowStateU(true)}} onMouseLeave={()=>{setShowStateU(false)}}>
                <div className='user-username'>{user.user.username}</div>
                <div className={`user-card ${showStateU ? 'user-show' : 'user-hidden'}`}>
                <div>{user.user.createdAt}</div>
                <div className='user-buttons'>
                <button className='button' onClick={()=>{setShowStateL(true)}}>Logout</button>
                <button className='button' onClick={()=>{setShowStateS(true)}}>UserSettings</button>
                </div>
                </div>
                </div>
                </div>
            ) : (
                <div>
               <button className='button' onClick={()=>navigate("/login")}>Log In</button>
               <button className='button' onClick={()=>navigate("/register")}>Sign In</button>
               </div>
            )}
             <Modal active={showStateL} setActive={setShowStateL} children={<Logout/>}/>
             <Modal active={showStateS} setActive={setShowStateS} children={<UserSettings/>} user={user.user} setUserOrig={setUser}/>
        </div>
        
    );
};

export default Navbar;
