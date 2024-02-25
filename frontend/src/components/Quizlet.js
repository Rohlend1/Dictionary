import React, { useState } from 'react';
import axios from 'axios';

const Quizlet = () => {
    const [data, setData] = useState([]);
    const [score, setScore] = useState(0);
    const [selectedCard, setSelectedCard] = useState(null);
    const [gameStarted, setGameStarted] = useState(false);
    let token = `Bearer ${localStorage.getItem("jwt")}`

    const fetchQuizData = async () => {
        try {
            const response = await axios.get('http://localhost:8080/quizlet', {
                headers: {
                    Authorization: token,
                },
            });

            setData(response.data);
            setScore(0);
            setSelectedCard(null);
            setGameStarted(true);
        } catch (error) {
            console.error('Ошибка при получении данных:', error);
        }
    };

    const handleCardClick = (translate, isAnswer) => {
        if (isAnswer) {
                setScore((prevScore) => prevScore + 1);
                document.body.style.backgroundColor = '#4caf50';
            } else {
                document.body.style.backgroundColor = '#f44336';
                setScore(0);
            }
        setSelectedCard({translate, isAnswer})

            setTimeout(() => {
                document.body.style.backgroundColor = '';
            }, 1000);
    };

    const renderCards = () => {
        return data.map((item, index) => (
            <div
                key={index}
                className={`card ${selectedCard && selectedCard.translate === item.translate ? 'selected' : ''}`}
                onClick={() => handleCardClick(item.translate, item.isAnswer)}
            >
                <p className="value">{item.value}</p>
                {(selectedCard && selectedCard.translate === item.translate) && (
                    <div className="translate-container">
                        <p className="translate">{item.translate}</p>
                    </div>
                )}
            </div>
        ));
    };

    const getQuestionValue = () => {
        const answerItem = data.find((item) => item.isAnswer);
        return answerItem ? `Как переводиться \"${answerItem.translate}\"?` : '';
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
            <div>
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
        <div>
            {renderGameContent()}
        </div>
    );
};

export default Quizlet;