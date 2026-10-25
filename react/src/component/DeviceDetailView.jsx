// Copyright (c) Microsoft Corporation.
// Licensed under the MIT License.

import React from 'react'
import 'bootstrap/dist/css/bootstrap.css'
import cssObj from '@/css/style.scss'
import '../css/common.css';
import '../css/androidPhone.css';
import '../css/iphonex.css';
import '../css/imac.css';
import '../css/androidTablet.css';
import '../css/DeviceDetailView.css';
import Tooltip from '@mui/material/Tooltip';
import Stack from "@mui/material/Stack";

const deviceStyle = {
    width: "210px",
    height: "420px"
};
const deviceStyleLand = {
    width: "418px",
    height: "264px"
};

const pcDeviceStyle = {
    width: "600px",
    height: "415px",
    paddingTop: "60px"
};

const tdStyle = {
    verticalAlign: "middle",
};

const map = {
    ONLINE: "badge-success",
    TESTING: "badge-primary",
    UNSTABLE: "badge-warning"
};

export default class DeviceDetailView extends React.Component {
    constructor(props) {
        super(props)
    }

    render() {
        const item = this.props.deviceItem;
        const displayStatus = item.status.toUpperCase()
        let badgeClass = "badge-danger";
        if (map[displayStatus]) {
            badgeClass = map[displayStatus]
        }

        return (
            <div className='deviceContainer'>
                {
                    item.type == "WINDOWS" ? this.getPCCase(item) : this.getProperPhoneCase(item)
                }
                <div>
                    <Tooltip title={
                        <Stack>
                            {
                                item.runningTestName ?
                                <span className="badge" style={{ fontSize: '0.8rem' }}>{_.truncate(item.runningTestName, 32)}</span> : null
                            }

                            <span className={"badge " + badgeClass} style={{ fontSize: "0.8rem" }}>{item.status}</span>

                            {
                                item.brand ?
                                <span className="badge badge-dark" style={{ fontSize: '0.8rem' }}> {item.brand.toUpperCase()}</span> : null
                            }

                            {
                                item.brand !== "Apple" ?
                                <span className="badge badge-warning" style={{ fontSize: '0.8rem', alignContent: "center" }}>API{item.osSDKInt}</span> : null
                            }

                            {
                                item.brand !== "Apple" ?
                                <span className="badge badge-primary" style={{ fontSize: '0.8rem' }}>{item.screenSize}</span> : null
                            }

                            {
                                item.brand !== "Apple" ?
                                <span className="badge badge-info" style={{ fontSize: '0.8rem' }}>DPI{item.screenDensity}</span> : null
                            }

                            {
                                item.deviceGroup ?
                                <span className='badge' style={{ fontSize: '0.8rem' }}>{item.deviceGroup.join(' ')}</span> : null
                            }
                            {/* ===== CHANGE FOR ISSUE #345 ===== */}
                            {
                                item.isPrivate ?
                                <span className='badge badge-secondary' style={{ fontSize: '0.8rem' }}>Private</span> : null
                            }
                            {/* ================================= */}
                        </Stack>
                    }>
                        {/* ===== CHANGE FOR ISSUE #345 ===== */}
                        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                                <span className={"badge " + badgeClass} style={{ fontSize: "1rem" }}>
                                    {this.getDeviceName(item)}
                                </span>
                                {item.isPrivate ?
                                    <span className="material-icons-outlined" style={{ fontSize: '1rem', color: '#ffd54f' }}>lock</span>
                                    : null
                                }
                            </div>
                            <span className="badge" style={{ fontSize: "0.6rem" }}>{item.serialNum}</span>
                        </div>
                        {/* ================================= */}
                    </Tooltip>
                </div>
            </div>
        )
    }

    getProperPhoneCase(item) {
        return item.brand === "Apple" ? this.getApplePhoneCase(item) : item.model == 'Surface Duo' ? this.getSurfaceDuoCase(item) : this.getAndroidPhoneCase(item)
    }

    getDeviceName(item) {
        return item.model == '-' ? item.name : item.model
    }

    getPCCase(item) {
        return <div className='deviceScreen' style={pcDeviceStyle}>
            <div className="imac black portrait">
                <div className="caseBorder" />
                <div className="case" />
                <div className="reflection" />
                <div className="screen" />
                <div className="stand" />
                <div className="base" />
                <div className="content">
                    <img className={cssObj.device_screenshot}
                        src={item.screenshotImageUrl}
                        alt={"Computer"} />
                </div>
            </div>
        </div>
    }

    getSurfaceDuoCase(item) {
        return <div className='deviceScreen' style={deviceStyleLand}>
            <div className="androidTablet black landscape">
                <div className="case" />
                <div className="content">
                    <img className={cssObj.tablet_screenshot}
                        src={item.screenshotImageUrl}
                        alt="Surface Duo" />
                </div>
            </div>
        </div>
    }

    getAndroidPhoneCase(item) {
        return <div className='deviceScreen' style={deviceStyle} onClick={()=>{ this.props.OnDeviceSelected(item.serialNum) }}>
            <div className="androidPhone black portrait">
                <div className="case" />
                <div className="camera" />
                <div className="content">
                    <img className={cssObj.mobile_screenshot}
                        src={item.screenshotImageUrl}
                        alt="Android Phone" 
                        />
                </div>
            </div>
        </div>
    }

    getApplePhoneCase(item) {
        return <div className='deviceScreen' style={deviceStyle}>
            <div className="iphonex white portrait">
                <div className="caseBorder" />
                <div className="case" />
                <div className="speaker" />
                <div className="content">
                    <img className={cssObj.iphone_mobile_screenshot}
                        src={item.screenshotImageUrl}
                        alt="iPhone" />
                </div>
            </div>
        </div>
    }
}