SUMMARY = "Score Scrample Recipe"
DESCRIPTION = "This recipe will build the SCORE-Scrample repository using Bazel."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2ee41112a44fe7014dce33e26468ba93"

inherit externalsrc

B = "${WORKDIR}/build"

DEPENDS += "bazel-native"

do_configure() {
    install -d ${S}/platforms
    install -d ${B}
    install -m 0644 \
            ${THISDIR}/files/platforms/BUILD.bazel \
            ${S}/platforms/BUILD.bazel

        install -m 0644 \
            ${THISDIR}/files/yocto.bazelrc \
            ${B}/yocto.bazelrc

    if ! grep -q 'score_bazel_cpp_toolchains' ${S}/MODULE.bazel; then
        cat >> ${S}/MODULE.bazel <<'EOF'

bazel_dep(name = "score_bazel_cpp_toolchains", version = "1.0.3")

gcc = use_extension(
    "@score_bazel_cpp_toolchains//extensions:gcc.bzl",
    "gcc",
)
gcc.toolchain(
    name = "score_gcc_toolchain",
    target_cpu = "aarch64",
    target_os = "linux",
    version = "12.2.0",
    use_default_package = True,
)
use_repo(gcc, "score_gcc_toolchain")
EOF
    fi
}

do_compile() {
    export CC="${CC}"
    export CXX="${CXX}"
    export LD="${LD}"
    export AR="${AR}"
    export STRIP="${STRIP}"

    cd ${S}
    bazel \
    --bazelrc="${B}/yocto.bazelrc" \
    --batch \
    --output_user_root="${B}/bazel-user-root" \
    --output_base="${B}/bazel-output-base" \
    build \
    --config=linux-aarch64 \
    //src:scrample
}
do_compile[network] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 \
            ${S}/bazel-bin/src/scrample \
            ${D}${bindir}/scrample

    install -d ${D}${sysconfdir}
    install -m 0644 \
            ${S}/src/etc/mw_com_config.json \
            ${D}${sysconfdir}/mw_com_config.json
}

FILES:${PN} = "${bindir}/scrample ${sysconfdir}/mw_com_config.json"
