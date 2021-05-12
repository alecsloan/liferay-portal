import React from 'react';
import ReactDOM from 'react-dom';
import QRCode from 'qrcode.react';

export default class extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			value: props.value
		};
	}

    render() {
        return (
            <div className="text-center">
                <h1>Test</h1>
				<QRCode value={value} />
            </div>
        );
    }
}