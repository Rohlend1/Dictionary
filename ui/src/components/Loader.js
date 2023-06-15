import React, { useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSpinner } from '@fortawesome/free-solid-svg-icons'
const Logout = () => {
    
    useEffect(() => {
    }, []);
    return (
            <div className="loading">
                <FontAwesomeIcon className='loading-icon' icon={faSpinner} />
                </div>
        
    );
};

export default Logout;
