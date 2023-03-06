import { useState, useEffect } from 'react'
import DisplayJha from './DisplayJha';
import Container from 'react-bootstrap/Container';

import axios from 'axios'

const AllJobHazards = () => {
    const [jhaData, setJhaData] = useState([]);

    useEffect(() => {
        const getAllJha = async () => {
            const response = await axios.get("http://localhost:8080/jha/all");
            if (response.status === 200){
                const jhaInfo = response.data.map(jha => {
                    return jha;
                });
                setJhaData(jhaInfo);
            }
            else {
                console.log("The bad response is", response);
            }
        }
        getAllJha();
    }, []);

    const deleteJha = async (deleteTitle) => {
        const response = await axios.delete(`http://localhost:8080/jha/${deleteTitle}`);
        if (response.status === 204) {
            const newJhaData = jhaData.filter(jha => {
                if (jha.title !== deleteTitle){
                    return jha;
                }
            });
            setJhaData(newJhaData);
        } else {
            console.log("Error with delete JHA")
        }
    }
   
    return(
        <Container>
            <h5 className='py-1'>Current Job Hazard Analyses</h5>
            {jhaData.map(jha => {
                return (<DisplayJha title={jha.title} author={jha.author} deleteJha={() => {deleteJha(jha.title)}}/>)
            })}
        </Container>
    )
}

export default AllJobHazards;