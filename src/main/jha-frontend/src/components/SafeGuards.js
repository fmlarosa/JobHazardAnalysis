import { useState, useEffect } from 'react'
import axios from 'axios'


const SafeGuards = (props) => {
    const [safeGuards, setSafeGuards] = useState([]);

    useEffect(() => {
        const getSafeGuards = async () => {
            const response = axios.get(`http://localhost:8080/safeGuard/${props.hazardID}`)
            try {
                const hazardSafeGuards = (await response).data.map(safeGuard => {
                    return safeGuard;
                })
                setSafeGuards(hazardSafeGuards);
            } catch (err){
                console.log("Error retrieving safeGuards", err);
            }
        }
        getSafeGuards();
    }, [props.hazardID]);
    return (
        <>
            <p><b>SafeGuards:</b></p>
            <ul>  
                {safeGuards.map(safeGuard => {
                    return  (<li>{safeGuard.safetyPrecaution}</li>)
                })}
            </ul>
        </>
    )
}

export default SafeGuards;