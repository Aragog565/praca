import React, { useState, useEffect } from 'react';
import './Footer.css'
export function Footer(){
  const [currentDate, setCurrentDate] = useState(new Date());
  const userLanguage = navigator.language;
  const [userCountryCode, setUserCountryCode] = useState("");
  const [userCurrency, setUserCurrency] = useState("");
  

  useEffect(() => {
    const intervalId = setInterval(() => {
      setCurrentDate(new Date());
    }, 1000);
    return () => clearInterval(intervalId);
  }, []);

    return(
        
        <footer className='stopka'>
                <div className='info'>
                <h4>Centrum pomocy</h4>
                <li onClick={() => window.open('https://www.google.com', '_blank')}>O nas</li>
                <li>Pomoc</li>
                <li>Aktualności</li>
                <li>Regulamin</li>
                <li>Bezpieczeństow</li>
                </div>
                <div className='witryna'>
                <h4>Ustawienia lokalne</h4>
                <li value={userCountryCode}>Kraj: {userCountryCode==='PL' ? 'Polska': userCountryCode ? userCountryCode : "..."}</li>
                <li value={userLanguage}>Język: {userLanguage}</li>
                <li>Waluta: {userCurrency ? userCurrency : "..."}</li>
                </div>
        </footer>
    )
}