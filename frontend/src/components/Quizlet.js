import React, {useRef, useState} from 'react';
import axios from 'axios';

const Quizlet = () => {
    const [data, setData] = useState([]);
    const [score, setScore] = useState(0);
    const [selectedCard, setSelectedCard] = useState(null);
    const [gameStarted, setGameStarted] = useState(false);
    const [roundEnded, setIsRoundEnded] = useState(false)
    const quiz = useRef(null)
    let token = `Bearer ${localStorage.getItem("jwt")}`

    const fetchQuizData = async () => {
        try {
            const response = await axios.get('http://localhost:8080/quizlet', {
                headers: {
                    Authorization: token,
                },
            });
            setIsRoundEnded(false)
            setData(response.data);
            setScore(0);
            setSelectedCard(null);
            setGameStarted(true);
        } catch (error) {
            console.error('Ошибка при получении данных:', error);
        }
    };

    const handleCardClick = (item) => {
        if (roundEnded) return
        if (item.isAnswer) {
                setScore((prevScore) => prevScore + 1);
                quiz.current.style.backgroundColor = 'rgba(0,133,5,0.7)';
                setIsRoundEnded(true)
            } else {
                quiz.current.style.backgroundColor = 'rgba(225,0,0,0.7)';
                setScore(0);
                setIsRoundEnded(true)
            }
        setSelectedCard(item)

            setTimeout(() => {
                quiz.current.style.backgroundColor = '';
            }, 700);
    };

    const renderCards = () => {
        return data.map((item, index) => (
            <div
                key={index}
                className={`card ${selectedCard && selectedCard.translate === item.translate ? 'selected' : ''}`}
                onClick={() => handleCardClick(item)}
            >
                <p className="value">{item.value}</p>
                {(selectedCard && selectedCard.translate === item.translate || roundEnded) && (
                    <div className={`translate-container ${roundEnded ? "show-translate" : ""}`}>
                        <p className="translate">{item.translate}</p>
                    </div>
                )}
            </div>
        ));
    };

    const getQuestionValue = () => {
        const answerItem = data.find((item) => item.isAnswer);
        return answerItem ? `Как переводится \"${answerItem.translate}\"?` : '';
    };

    const renderGameContent = () => {
        if (!gameStarted) {
            return (
                <div className="quiz-button-container">
                    <button className="quiz-button" onClick={fetchQuizData}>Начать</button>
                </div>
            );
        }

        if (data.length === 0) {
            return (
                <div className="quiz-button-container">
                    <p>Уровни закончились! Поздравляем!</p>
                    <button className="quiz-button" onClick={fetchQuizData}>Начать заново</button>
                </div>
            );
        }

        return (
            <div ref={quiz} className="quiz-main-wrapper">
                <p className="score-label">Счет: {score}</p>
                <div className="quiz-container">
                    <div className="question-box">
                        <p className="question-value">{getQuestionValue()}</p>
                    </div>
                    <div className="card-container">
                        <div className="card-grid">{renderCards()}</div>
                    </div>
                </div>
                <div className="quiz-button-container">
                    <button className="quiz-button" onClick={fetchQuizData}>Следующий уровень</button>
                </div>
            </div>
        );
    };
    return (
        <>
            {renderGameContent()}
        </>
    );
};

export default Quizlet;