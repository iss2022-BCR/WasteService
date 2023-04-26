function validateConnect()
{
    let ipaddress = $("#ip").val()
    let port = $("#port").val()

    $("#connect").prop('disabled', !(validateIPaddress(ipaddress) && validatePort(port)));
    return (validateIPaddress(ipaddress) && validatePort(port))
}
function validateIPaddress(ipaddress)
{
    if (ipaddress === "localhost")
        return true
    if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipaddress))
        return true

    return false
}
function validatePort(port)
{
    return (port > 1023 && port <= 65535)
}