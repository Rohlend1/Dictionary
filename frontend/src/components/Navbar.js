import React, { useEffect, useState } from 'react';
import UserSettings from './UserSettings';
import Modal from './Modal'
import Logout from './Logout';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUser } from '@fortawesome/free-solid-svg-icons'
const Navbar = (user,setUser) => {
    const [showStateU,setShowStateU] = useState(false)
    const [showStateL,setShowStateL] = useState(false)

    
    return (
            <div className="navbar">
             <Modal active={showStateU} setActive={setShowStateU} children={<UserSettings/>} user={user.user} setUserOrig={setUser}/>
             <Modal active={showStateL} setActive={setShowStateL} children={<Logout/>}/>
            {user.user !== undefined ? (
                 <div className="user">
                <div className='user-contents' onClick={()=>{setShowStateU(true)}}>
                <FontAwesomeIcon icon={faUser} />
                <div className='user-username'>{user.user.username}</div>
                </div>
                <button className="button" onClick={()=>{setShowStateL(true)}}>Logout</button>
                </div>
            ) : (
                <div>
               </div>
            )}
        </div>
        
    );
};

export default Navbar;
    