1.vmx增加

```
monitor_control.virtual_rdtsc = "false"
monitor_control.restrict_backdoor = "true"
isolation.tools.getPtrLocation.disable = "true"
isolation.tools.setPtrLocation.disable = "true"
isolation.tools.setVersion.disable = "true"
isolation.tools.getVersion.disable = "true"
monitor_control.disable_directexec = "true"
SMBIOS.reflectHost = "true"
ethernet0.connectionType = "nat"
usb_xhci:4.present = "TRUE"
usb_xhci:4.deviceType = "hid"
usb_xhci:4.port = "4"
usb_xhci:4.parent = "-1"
```

2.卸载vmtools

3.修改注册表

HKEY_LOCAL_MACHINE/SYSTEM/CurrentControlSet/Control/Class/{4D36E968-E325-11CE-BFC1-08002BE10318}/0000

GTX 950