SUMMARY = "Bazel-Native 8.7.0"
DESCRIPTION = "The Bazel-Native layer offers the Bazel 8.7.0 binary"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# This is the host running BitBake, not the Raspberry Pi target.
BAZEL_VERSION = "8.7.0"
BAZEL_HOST = "linux-x86_64"

SRC_URI = "https://releases.bazel.build/${BAZEL_VERSION}/release/bazel-${BAZEL_VERSION}-${BAZEL_HOST};downloadfilename=bazel"
SRC_URI[sha256sum] = "d7606e679b78067c811096fb3d6cf135225b528835ca396e3a4dddf957859544"
S = "${WORKDIR}"

inherit native

do_install() {
    install -d ${D}${bindir}
    install -m 0644 \
                ${S}/bazel \
                ${D}${bindir}/bazel.bin
    install -m 0755 /dev/null ${D}${bindir}/bazel
    printf '%s\n' \
        '#!/bin/sh' \
        'set -e' \
        'bazel_bin="$(dirname "$0")/bazel.bin"' \
        'chmod +x "$bazel_bin"' \
        'exec "$bazel_bin" "$@"' \
        > ${D}${bindir}/bazel
}
FILES:${PN} += "${bindir}/bazel ${bindir}/bazel.bin"
