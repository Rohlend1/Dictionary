import React, { useEffect, useState } from 'react';
const Logout = (value,translate) => {
    return (
            <div className="logout">
                <div className='logout-title'>{value}</div>
                <div className='logout-title'>{translate}</div>
                </div>
        
    );
};

export default Logout;
