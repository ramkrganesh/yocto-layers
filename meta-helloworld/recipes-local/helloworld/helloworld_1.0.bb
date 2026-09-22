SUMMARY = "Hello World Program"
DESCRIPTION = "A simple first program to print Hello World after yocto build"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://helloworld.cpp"
S = "${WORKDIR}"

do_compile () {
    ${CXX} ${CPPFLAGS} ${CXXFLAGS} ${LDFLAGS} helloworld.cpp -o helloworld
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 helloworld ${D}${bindir}
}
