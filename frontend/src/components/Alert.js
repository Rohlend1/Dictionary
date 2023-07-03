import { useEffect } from "react";

const Alert = ({isShow,setIsShow,children}) => {
  useEffect(()=>{
    let Debounce = setTimeout(()=>{
        setIsShow(false)
    },1000)
    return () => {
        clearTimeout(Debounce)
    }
},[isShow])
  return (
      <div className={!isShow ? "alert shadow" : "alert shadow show"} onClick={()=>{setIsShow(false)}}>
          <div className="" onClick={(e)=>e.stopPropagation()}>
              {children}
          </div>
  </div>
  );
};

export default Alert;
