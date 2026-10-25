// Copyright (c) Microsoft Corporation.
// Licensed under the MIT License.

import React from 'react';
import DeviceDialog from '@/component/DeviceDialog';
import 'bootstrap/dist/css/bootstrap.css'
import axios from '@/axios'
import DeviceDetailView from '@/component/DeviceDetailView';
import Collapse from '@mui/material/Collapse';
import Typography from "@material-ui/core/Typography";
import LoadingButton from "@mui/lab/LoadingButton";
import Skeleton from "@mui/material/Skeleton";
import BaseView from "@/component/BaseView";
import IconButton from "@material-ui/core/IconButton";
import { default as MUITooltip } from "@mui/material/Tooltip";
import Stack from "@mui/material/Stack";
import Button from "@mui/material/Button";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";
import '../css/deviceAgentsView.css';
import _, { get } from 'lodash';
import { AreaChart, Area, XAxis, YAxis, PieChart, Tooltip, Pie, Cell, Legend, ReferenceLine } from 'recharts';
import { string } from 'prop-types';
const COLORS = ['#00C49F', '#90C12F', '#44C16F', '#00C12F', '#00612F', '#59C12F', '#0061FF', '#0061aa'];
const RADIAN = Math.PI / 180;
const PieCustomizedLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, percent }) => {
    const radius = innerRadius + (outerRadius - innerRadius) * 0.75;
    const x = cx + radius * Math.cos(-midAngle * RADIAN);
    const y = cy + radius * Math.sin(-midAngle * RADIAN);

    return (
        <text x={x} y={y} fill="white" fontSize={12} textAnchor={x > cx ? 'start' : 'end'}
            dominantBaseline="central">
            {`${(percent * 100).toFixed(1)}%`}
        </text>
    );
};

export default class DeviceAgentsView extends BaseView {
    state = {
        agents: null,
        collapseStatus: {},
        refreshing: false,
        latestAgentVersion: null,
        fileId: null,
        agentUpdateStatus: "UPDATING",
        openDevice: false,
        selectedDevice: {},
    }

    render() {
        const { agents, refreshing } = this.state;
        const { snackbarIsShown, snackbarSeverity, snackbarMessage } = this.state
        const agentRows = []
        const OnDeviceSelected = (deviceSerialnum)=>{
            var device = agents.flatMap(agent=>agent.devices).find(device=>device.serialNum===deviceSerialnum)
            this.setState({
                openDevice:true,
                selectedDevice:device,
            })
        }
        var agentChartData = []
        var deviceChartData = []
        var deviceStatusChartData = []
        var deviceModelChartData = []
        var deviceCount = "-"
        var agentCount = "-"
        var overallPieSize = 0
        if (agents) {
            const folderHeadBgColor = '#1565c0'
            deviceCount = _.sumBy(agents, function (o) { return o.devices.length; })
            agentCount = agents.length

            var agentCountByOS = _.countBy(agents, function (o) { return o.agentOS; })
            for (var k in agentCountByOS) {
                var c = agentCountByOS[k]
                if (k === "null" || k == 0 || k == "0" || !k) {
                    k = "Unknown"
                    if (agentChartData["Unknown"])
                        c += agentChartData["Unknown"]
                }
                agentChartData.push({ name: k, count: c })
            }

            let mergedDeviceList = _.flatMap(agents, item => item.devices);
            var deviceCountByType = _.countBy(mergedDeviceList, function (o) { return o.type; })
            for (var k in deviceCountByType) {
                var c = deviceCountByType[k]
                if (k === "null" || k == 0 || k == "0" || !k) {
                    k = "Unknown"
                    if (deviceChartData["Unknown"])
                        c += deviceChartData["Unknown"]
                }
                deviceChartData.push({ name: k, count: c })
            }

            var deviceCountByStatus = _.countBy(mergedDeviceList, function (o) { return o.status; })
            for (var k in deviceCountByStatus) {
                var c = deviceCountByStatus[k]
                if (k === "null" || k == 0 || k == "0" || !k) {
                    k = "Unknown"
                    if (deviceStatusChartData["Unknown"])
                        c += deviceStatusChartData["Unknown"]
                }
                deviceStatusChartData.push({ name: k, count: c })
            }

            // var deviceCountByModel = _.countBy(mergedDeviceList, function (o) { return o.model; })
            // for (var k in deviceCountByModel) {
            //     var c = deviceCountByModel[k]
            //     if (k === "null" || k == 0 || k == "0" || !k) {
            //         k = "Unknown"
            //         if (deviceModelChartData["Unknown"])
            //             c += deviceModelChartData["Unknown"]
            //     }
            //     deviceModelChartData.push({ name: k, count: c })
            // }

            overallPieSize = 420

            agents.map(
                (agent) => {
                    agentRows.push(
                        <div class='deviceAgents-agent'>
                            <div
                                class='deviceAgents-agentBanner'
                                style={{ backgroundColor: folderHeadBgColor }}
                                onClick={() => this.changeCollapseStatus(agent.agentId)}>

                                <div style={{ color: 'white', fontSize: 'large', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '6px' }}>
                                    {agent.agentName}: {agent.devices.length}
                                    {agent.teamId ?
                                        <MUITooltip title={"Private — Team: " + agent.teamName}>
                                            <span className="material-icons-outlined" style={{ fontSize: '1.1rem', color: '#ffd54f' }}>lock</span>
                                        </MUITooltip>
                                        : null
                                    }
                                </div>

                                <div class='deviceAgents-agentBanner-tail'>
                                    <div style={{ color: 'white' }}>
                                        {
                                            <div>
                                                {
                                                    Number(agent.agentVersionCode) >= Number(this.state.latestAgentVersion) ?
                                                        <MUITooltip
                                                            title={
                                                                <Stack>
                                                                    Host:{agent.hostname}<br />
                                                                    Version:{agent.agentVersionName}<br />
                                                                    OS:{agent.agentOS}
                                                                </Stack>}
                                                            style={{ padding: "0" }}>
                                                            <IconButton>
                                                                <span style={{ color: 'white', }}
                                                                    className="material-icons-outlined">info</span>
                                                            </IconButton>
                                                        </MUITooltip>
                                                        :
                                                        agent.agentStatus === "UPDATING" ?
                                                            <MUITooltip
                                                                title={this.state.agentUpdateStatus}
                                                                onOpen={() => this.getUpdateStatus(agent.agentId)}
                                                                style={{ padding: "0" }}>
                                                                <Button color="inherit" size="small" style={{ padding: "0" }}>
                                                                    Updating
                                                                </Button>
                                                            </MUITooltip>
                                                            :
                                                            <Button variant="contained" color="error" size="small"
                                                                style={{ padding: "0" }}
                                                                onClick={() => this.updateAgent(agent)}>
                                                                Update
                                                            </Button>
                                                }
                                                <MUITooltip
                                                    title={
                                                        <Stack>
                                                            <div style={{ userSelect: 'none', padding: 2, fontSize: '0.8rem' }}>
                                                                Drivers:
                                                                {
                                                                    agent.functionAvailabilities
                                                                        .filter(
                                                                            (fa) => fa.functionType === "DEVICE_DRIVER"
                                                                        )
                                                                        .sort(
                                                                            (fa1, fa2) => {
                                                                                var s1 = 0
                                                                                var s2 = 0
                                                                                s1 += fa1.available ? 1 : 0
                                                                                s1 += fa1.enabled ? 1 : 0
                                                                                s2 += fa2.available ? 1 : 0
                                                                                s2 += fa2.enabled ? 1 : 0
                                                                                return s2 - s1
                                                                            }
                                                                        )
                                                                        .map(
                                                                            (fa) => this.convertFunctionAvailability(fa)
                                                                        )
                                                                }
                                                            </div>
                                                            <div style={{ userSelect: 'none', padding: 2, fontSize: '0.8rem' }}>
                                                                Runners:
                                                                {
                                                                    agent.functionAvailabilities
                                                                        .filter(
                                                                            (fa) => (fa.functionType === "TEST_RUNNER" || fa.functionType === "ANALYSIS_RUNNER")
                                                                        )
                                                                        .sort(
                                                                            (fa1, fa2) => {
                                                                                var s1 = 0
                                                                                var s2 = 0
                                                                                s1 += fa1.available ? 1 : 0
                                                                                s1 += fa1.enabled ? 1 : 0
                                                                                s2 += fa2.available ? 1 : 0
                                                                                s2 += fa2.enabled ? 1 : 0
                                                                                return s2 - s1
                                                                            }
                                                                        )
                                                                        .map(
                                                                            (fa) => this.convertFunctionAvailability(fa)
                                                                        )
                                                                }
                                                            </div>
                                                        </Stack>}
                                                    style={{ padding: 0, paddingLeft: 4 }}>
                                                    <IconButton>
                                                        <span style={{ color: 'white', }}
                                                            className="material-icons-outlined">domain_verification</span>
                                                    </IconButton>
                                                </MUITooltip>
                                            </div>
                                        }
                                    </div>
                                    <div style={{ color: 'white', minWidth: 200 }}>
                                        {agent.userName}
                                    </div>
                                    <div style={{ color: 'white' }}>
                                        {
                                            this.state.collapseStatus[agent.agentId] ?
                                                <span className="material-icons-outlined">expand_less</span> :
                                                <span className="material-icons-outlined">expand_more</span>
                                        }
                                    </div>
                                </div>
                            </div>
                            <Collapse in={this.state.collapseStatus[agent.agentId]}>
                                <div className='deviceAgents-devicesList'>
                                    {
                                        agent.devices.map((d) => {
                                            return <DeviceDetailView deviceItem={d} OnDeviceSelected={OnDeviceSelected} hover/>
                                        })
                                    }
                                </div>
                            </Collapse>
                        </div>)
                })
        }
        return (
            <div className='deviceAgents'>
                <div className='deviceAgents-header'>
                    <Typography variant="h4" className="mt-2 mb-2">Connected Devices</Typography>
                    <div style={{ display: 'flex', gap: '16px' }}>
                        <Button
                            variant="outlined"
                            className="pl-4 pr-4"
                            endIcon={<span className="material-icons-outlined">auto_awesome_motion</span>}
                            onClick={this.setAllCollapseStatus}>
                            Collapse
                        </Button>
                        <LoadingButton
                            variant="contained"
                            className="pl-4 pr-4"
                            loading={refreshing}
                            loadingPosition="end"
                            endIcon={<span className="material-icons-outlined">sync</span>}
                            onClick={this.updateDeviceListData}>
                            Refresh
                        </LoadingButton>
                    </div>
                </div>
                <div className='deviceAgents-header'>
                    <Typography variant="h6" className="mt-2 mb-2" align='left'>
                        <span className="badge badge-primary">{agentCount}</span> agents, <span className="badge badge-success">{deviceCount}</span> devices
                    </Typography>
                </div>
                <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }} className="deviceAgents-header mt-2 mb-2">
                    <div>
                        <Typography variant="h6" className="mt-2 mb-2" align='center'>Test Agent OS</Typography>
                        <PieChart width={overallPieSize} height={overallPieSize}
                            title="Test Agent OS">
                            <Pie
                                data={agentChartData}
                                labelLine={false}
                                label={PieCustomizedLabel}
                                dataKey="count">
                                {agentChartData.map((entry, index) => (
                                    <Cell key={`cell-${index}`}
                                        fill={COLORS[index % COLORS.length]} />
                                ))}
                            </Pie>
                            <Legend layout="horizontal" verticalAlign="bottom" align="center" />
                            <Tooltip />
                        </PieChart>
                    </div>
                    <div>
                        <Typography variant="h6" className="mt-2 mb-2" align='center'>Test Device OS</Typography>
                        <PieChart width={overallPieSize} height={overallPieSize}>
                            <Pie
                                data={deviceChartData}
                                labelLine={false}
                                label={PieCustomizedLabel}
                                dataKey="count">
                                {deviceChartData.map((entry, index) => (
                                    <Cell key={`cell-${index}`}
                                        fill={COLORS[index % COLORS.length]} />
                                ))}
                            </Pie>
                            <Legend layout="horizontal" verticalAlign="bottom" align="center" />
                            <Tooltip />
                        </PieChart>
                    </div>
                    <div>
                        <Typography variant="h6" className="mt-2 mb-2" align='center'>Test Device State</Typography>
                        <PieChart width={overallPieSize} height={overallPieSize}>
                            <Pie
                                data={deviceStatusChartData}
                                labelLine={false}
                                label={PieCustomizedLabel}
                                dataKey="count">
                                {deviceStatusChartData.map((entry, index) => (
                                    <Cell key={`cell-${index}`}
                                        fill={COLORS[index % COLORS.length]} />
                                ))}
                            </Pie>
                            <Legend layout="horizontal" verticalAlign="bottom" align="center" />
                            <Tooltip />
                        </PieChart>
                    </div>

                    {/* <PieChart width={overallPieSize} height={overallPieSize}>
                        <Pie
                            data={deviceModelChartData}
                            labelLine={false}
                            label={PieCustomizedLabel}
                            dataKey="count">
                            {deviceModelChartData.map((entry, index) => (
                                <Cell key={`cell-${index}`}
                                    fill={COLORS[index % COLORS.length]} />
                            ))}
                        </Pie>
                        <Legend layout="horizontal" verticalAlign="bottom" align="center" />
                        <Tooltip />
                    </PieChart> */}
                </div>
                <DeviceDialog
                    open={this.state.openDevice}
                    onClose={() => this.handleCloseDeviceDialog()}
                    selectedDevice={this.state.selectedDevice}
                />
                {agentRows}
                <Snackbar
                    anchorOrigin={{
                        vertical: 'top',
                        horizontal: 'center'
                    }}
                    open={snackbarIsShown}
                    autoHideDuration={3000}
                    onClose={() => this.handleStatus("snackbarIsShown", false)}>
                    <Alert
                        onClose={() => this.handleStatus("snackbarIsShown", false)}
                        severity={snackbarSeverity}>
                        {snackbarMessage}
                    </Alert>
                </Snackbar>
            </div>
        )
    }

    changeCollapseStatus(agentId) {
        const collapseStatusNew = this.state.collapseStatus
        collapseStatusNew[agentId] = !this.state.collapseStatus[agentId]
        this.setState({
            collapseStatus: collapseStatusNew,
        })
    }

    setAllCollapseStatus = () => {
        const collapseStatusNew = this.state.collapseStatus
        for (const key in collapseStatusNew) {
            collapseStatusNew[key] = false
        }
        this.setState({
            collapseStatus: collapseStatusNew,
        })
    }

    updateDeviceListData = () => {
        this.setState({
            refreshing: true,
            agents: null,
        })
        axios.get('/api/device/list').then(res => {
            if (res.data && res.data.code === 200) {
                console.log(res.data)
                const agents = res.data.content
                const collapseStatus = {}
                let tokenPromises = [];
                for (let i = 0; i < agents.length; i++) {
                    agents[i].groupedDevices = []
                    const sliceLen = agents[i].agentDeviceType == 2 ? 2 : 4;
                    for (let j = 0; j < agents[i].devices.length; j += sliceLen) {
                        agents[i].groupedDevices.push(agents[i].devices.slice(j, j + sliceLen))
                    }
                    // get image url for each device
                    agents[i].devices.forEach((device) => {
                        if (device.imageRelPath) {
                            const p = this.getFileDownloadToken(device.imageRelPath).then((token) => {
                                device.screenshotImageUrl = (device.screenshotImageUrl || '') + '?' + token;
                            });
                            tokenPromises.push(p);
                        }
                    })
                    collapseStatus[agents[i].agentId] = true
                }
                // 等待所有 token 获取完毕后再 setState
                Promise.all(tokenPromises).then(() => {
                    this.setState({
                        agents: agents,
                        collapseStatus: collapseStatus,
                        refreshing: false,
                    })
                });
            } else {
                this.snackBarFail(res)
            }
        }).catch((error) => {
            this.snackBarError(error)
        })
    }

    getLatestAgentVersion = () => {
        axios.get('/api/center/info').then(res => {
            if (res.data && res.data.code === 200) {
                if (res.data.content && res.data.content.agentPkg) {
                    this.setState({
                        latestAgentVersion: res.data.content.agentPkg.fileParser.versionCode,
                        fileId: res.data.content.agentPkg.fileId
                    })
                }
            } else {
                this.snackBarFail(res)
            }
        }).catch((error) => {
            this.snackBarError(error)
        })
    }

    getUpdateStatus(agentId) {
        axios.get('/api/agent/getUpdateInfo/' + agentId).then(res => {
            if (res.data && res.data.code === 200) {
                let messages = res.data.content.updateMsgs
                console.log(messages)
                if (messages.at(-1) !== null) {
                    console.log(messages.at(-1).message)
                    this.setState({
                        agentUpdateStatus: messages.at(-1).message,
                    })
                }
            } else {
                this.snackBarFail(res)
            }
        }).catch((error) => {
            this.snackBarError(error)
        })
    }

    convertFunctionAvailability(availability) {
        var color = "YellowGreen"
        var icon = "check"
        var requires = null
        if (availability.available === false) {
            color = "DarkGray"
            icon = "close"
            requires = []
            availability.envCapabilityRequirements.map(
                (r) => {
                    if (r.ready === false) {
                        requires.push(r.envCapability.keyword)
                    }
                }
            )
        } else if (availability.enabled === false) {
            color = "OrangeRed"
            icon = "block"
        }

        var name = "";
        if (availability.functionName.endsWith("AndroidDeviceDriver")) {
            name = "Android"
        } else if (availability.functionName.endsWith("IOSDeviceDriver")) {
            name = "iOS"
        } else if ((availability.functionName.endsWith("WindowsDeviceDriver"))) {
            name = "Windows"
        } else if ((availability.functionName.endsWith("EspressoRunner"))) {
            name = "Espresso"
        } else if ((availability.functionName.endsWith("AppiumRunner"))) {
            name = "Appium"
        } else if ((availability.functionName.endsWith("AppiumCrossRunner"))) {
            name = "Appium Cross"
        } else if ((availability.functionName.endsWith("SmartRunner"))) {
            name = "Smart"
        } else if ((availability.functionName.endsWith("AdbMonkeyRunner"))) {
            name = "ADB Monkey"
        } else if ((availability.functionName.endsWith("AppiumMonkeyRunner"))) {
            name = "Appium Monkey"
        } else if ((availability.functionName.endsWith("T2CRunner"))) {
            name = "T2C"
        } else if ((availability.functionName.endsWith("XCTestRunner"))) {
            name = "XCTest"
        } else if ((availability.functionName.endsWith("MaestroRunner"))) {
            name = "Maestro"
        } else if ((availability.functionName.endsWith("PythonRunner"))) {
            name = "Python"
        } else if ((availability.functionName.endsWith("APK_SCANNER"))) {
            name = "APK Scanner"
        }

        return (
            <div style={{ display: 'flex', fontSize: '0.75rem', paddingLeft: 8, color: color, alignItems: 'center' }}>
                <span style={{ color: 'white', fontSize: '0.8rem', userSelect: 'none', marginRight: 4 }}
                    className="material-icons-outlined" >{icon}</span>
                <span style={{ userSelect: 'none' }}>{name}</span>
                {
                    requires === null ? null :
                        <MUITooltip
                            title={
                                <Stack>
                                    <text style={{ fontSize: '0.8rem' }}>Absent Dependency:</text>
                                    {
                                        requires.map(
                                            (r) => {
                                                return <span style={{ fontSize: '0.8rem', color: 'OrangeRed' }}>{r}</span>
                                            }
                                        )
                                    }
                                </Stack>}
                            placement='right'
                            style={{ padding: "0" }}>
                            <IconButton>
                                <span style={{ color: 'orange', fontSize: '1.05rem', marginLeft: 4 }} className="material-icons-outlined">info</span>
                            </IconButton>
                        </MUITooltip>
                }
            </div>
        )
    }

    updateAgent(agent) {
        const formParams = new URLSearchParams()
        formParams.append("agentId", agent.agentId)
        formParams.append("fileId", this.state.fileId)

        axios.post('/api/agent/updateAgent/', formParams, {
            headers: { 'content-type': 'application/x-www-form-urlencoded' }
        }).then(res => {
            if (res.data && res.data.code === 200) {
                agent.agentStatus = "UPDATING"
                this.setState({
                    snackbarSeverity: "success",
                    snackbarMessage: "Start updating agent",
                    snackbarIsShown: true,
                })
            } else {
                this.snackBarFail(res)
            }
        }).catch((error) => {
            this.snackBarError(error)
        })
    }

    componentDidMount() {
        // MOCK DATA for visual testing - remove before PR
        this.setState({
            latestAgentVersion: '999',
            agents: [
                {
                    agentId: 'agent-private-1',
                    agentName: 'Private Agent (Team A)',
                    agentOS: 'Linux',
                    agentVersionCode: '999',
                    agentVersionName: '1.0.0',
                    agentStatus: 'HEALTHY',
                    teamId: 'team-001',
                    teamName: 'Team Alpha',
                    userName: 'user@example.com',
                    hostname: 'host-private',
                    ip: '192.168.1.1',
                    functionAvailabilities: [],
                    devices: [
                        { id: 'd1', serialNum: 'PRIV001', name: 'Pixel 6', model: 'Pixel 6', status: 'ONLINE', brand: 'Google', type: 'ANDROID', isPrivate: true, osSDKInt: '31', screenSize: '1080x2400', screenDensity: 420, deviceGroup: ['groupA'] },
                        { id: 'd2', serialNum: 'PUB002', name: 'Galaxy S21', model: 'Galaxy S21', status: 'ONLINE', brand: 'Samsung', type: 'ANDROID', isPrivate: false, osSDKInt: '30', screenSize: '1080x2400', screenDensity: 400, deviceGroup: [] },
                    ]
                },
                {
                    agentId: 'agent-public-2',
                    agentName: 'Public Agent (No Team)',
                    agentOS: 'Windows',
                    agentVersionCode: '999',
                    agentVersionName: '1.0.0',
                    agentStatus: 'HEALTHY',
                    teamId: null,
                    teamName: null,
                    userName: 'admin@example.com',
                    hostname: 'host-public',
                    ip: '192.168.1.2',
                    functionAvailabilities: [],
                    devices: [
                        { id: 'd3', serialNum: 'PUB003', name: 'Pixel 5', model: 'Pixel 5', status: 'TESTING', brand: 'Google', type: 'ANDROID', isPrivate: false, osSDKInt: '30', screenSize: '1080x2340', screenDensity: 432, deviceGroup: [] },
                    ]
                }
            ],
            collapseStatus: { 'agent-private-1': true, 'agent-public-2': true },
            refreshing: false,
        })
    }

    handleCloseDeviceDialog() {
        this.setState({
            openDevice:false
        })
    }
}