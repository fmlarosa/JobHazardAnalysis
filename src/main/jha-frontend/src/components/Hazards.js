import { useState, useEffect } from 'react'
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import axios from 'axios'
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';
import '../styles/EditJha.css'


const Hazards = (props) => {
    const [hazards, setHazards] = useState([]);
    const [newHazard, setNewHazard] = useState('');

    useEffect(() => {
        const getHazards = async () => {
            const response = axios.get(`http://localhost:8080/hazard/${props.taskID}`)
            try {
                const jhaHazards = (await response).data.map(task => {
                    return task;
                })
                setHazards(jhaHazards);
            } catch (err){
                console.log("Error retrieving hazards", err);
            }
        }
        getHazards();
    }, [hazards]);

    const makeNewHazard = async () => {
        // eslint-disable-next-line no-restricted-globals
        event.preventDefault();
        const response = await axios.post(`http://localhost:8080/hazard/${props.taskID}`, null, {params: { danger: newHazard}});
        try {
            const addNewHazard = hazards.concat(response.data);
            setHazards(addNewHazard);
            setNewHazard('');
        } catch (err) {
            console.log("The response message is ", response.message)
        }
    }
 
    const setUpdateInfo = (hazardID) => {
        props.setUpdateType();
        props.setUpdateID(hazardID);
        props.visibility(true);
    }

    const deleteHazard = async (hazardID, danger) => {
        const response = await axios.delete(`http://localhost:8080/hazard/${hazardID}`, null, {params: { danger: danger}});
        try {
            if (response.status === 204) {
                const newHazards = hazards.filter(hazard => {
                    if (hazard.hazardID !== hazardID){
                        return hazard;
                    }
                });
                setHazards(newHazards);
            }
        }
        catch (err) {
            console.log("Error with deleting Task: ", err);
        }
    }

    

    return(
        <Container>
            <Row className="d-flex flex-wrap">
                <p className="mb-0"><b>Hazards:</b></p>
                <ul>
                {hazards.map((hazard) => {
                    return  (<div><Button className="btn btn-link rounded-circle" variant="link" onClick={() => setUpdateInfo(hazard.hazardID)}><i class="fas fa-regular fa-file-pen fa-2xs"></i></Button><li>
                                 {hazard.danger} 
                            </li><Button className="btn btn-link rounded-circle text-danger" id="deleteButton" variant="link" onClick={() => deleteHazard(hazard.hazardID, hazard.danger)}><i class="fas fa-solid fa-x fa-2xs"></i></Button></div>)
                    })}
                </ul>
            </Row>
            <Row>
                <Form onSubmit={makeNewHazard}>
                    <Row>
                        <Col>
                            <Form.Group className="mb-3">
                                <Form.Control type="text" required={true} value={newHazard} placeholder="Enter New Hazard" onChange={(event) => setNewHazard(event.target.value)}/>
                            </Form.Group>
                        </Col>
                    </Row>
                </Form>
            </Row>
        </Container>
    )
}

export default Hazards;
