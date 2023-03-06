import axios from 'axios'
import Button from 'react-bootstrap/Button';
import { useState } from 'react'
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

const UpdateComponent = ({visibility, updateType, updateID}) => {
    const [newComponent, setNewComponent] = useState('');

    const elementType = (updateType === 'hazard') ? 'danger' : 'safetyPrecaution';

    const update = async () => {
        // eslint-disable-next-line no-restricted-globals
        event.preventDefault();
        axios.put(`http://localhost:8080/${updateType}/${updateID}`, null, {params: { [elementType]: newComponent}})
        .then(response => {
            visibility();
            setNewComponent('');
        })
    }

    const cancel = () => {
        visibility();
        setNewComponent('');
    }

    return (
        <div id="popupBG">
            <Form  id="popupForm" onSubmit={update}>
                <Row>
                    <Col>
                        <Form.Group className="mb-3">
                            <Form.Control type="text" required={true} value={newComponent} placeholder="Enter New value here" onChange={(event) => setNewComponent(event.target.value)}/>
                        </Form.Group>
                    </Col>
                </Row>
                <Row className="d-flex justify-content-between">
                    <Col>
                        <Button variant="secondary" type="submit">
                            Update {updateType}
                        </Button>
                    </Col>
                    <Col className="d-flex justify-content-end">
                        <Button variant="danger" onClick={cancel}>
                            Cancel
                        </Button>
                    </Col>
                </Row>
            </Form>   
        </div>
    )
}

export default UpdateComponent;