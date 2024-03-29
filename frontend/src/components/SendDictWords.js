import React, { useEffect } from 'react';
import axios from 'axios';
const SendDictWords = ({words,setWords,setAllWords}) => {
    const link = process.env.REACT_APP_LINK
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`

    const sendDictionatiesWords = async () => {

        try {
            const response = await axios.post(`${link}/dict/add/words`,{ words: words },
                {
                    headers: {
                        'Authorization': Authorization
                    }
                }
            );
            window.location.reload();
            console.log(response.data)
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
    };
}

      const handleDeleteWord = (word) => {
        let element = words.find(e => e.value === word)
        setWords(prevDictWords => {
          const updatedDictWords = prevDictWords.filter(e => e.value !== word);
          return updatedDictWords;
        });
        setAllWords(prevWords => [element,...prevWords]);
      }
      
    useEffect(() => {
    }, []);

    return (
        <div className="send-words-container">
        {words && words.length > 0 ? (
                    <div className = "block">
                <div className="words-list-container-del">
                    <div className="words-list-del" >
                        {words.map((word) => (
                            <div key={word.value} className="word-item">
                                <button  onClick={()=>handleDeleteWord(word.value)} value={word.value} className="word-value-send" >
                                    <div>{word.value}</div>
                                    <div>{word.translate}</div>
                                    </button>
                            </div>
                        ))}
                    </div>
                </div>
                <div className = "bottom-line">
                <button className = "button button-bottom" style={{marginTop:"5px"}} onClick={sendDictionatiesWords}>Добавить слова</button>
                </div>
                </div>
           ) : (   
            <div className = "block">
                <div style={{textAlign:"center"}}>Добавьте слова</div>
                </div>
            )}
    </div>
    );
};

export default SendDictWords;
