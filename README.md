## What is this? 
This is a collection of hacky scripts for setting up a production-like single node 
Kubernetes cluster for local development. It also includes a demo echo server 
written in Java 10. The end result is a production like cluster with wildcard 
domain ingress setup (*.kube.dev) and TLS termination. 

## Prerequisites:
1. **Linux** and **Make** (preferably Ubuntu 18.04 or compatible distro as that's what was used, 
the VM itself is on 16.04 due to K8S' incompatibility with 18.04), this has not been tested on OSX or Windows. 
2. **DNSMasq** - for wildcard domain resolution. 
3. **OpenSSH** for generating CA and server certificates.
4. **Docker** for building images locally. 
5. **Kubectl** and **Helm** for managing the K8S deployment and dependencies. 
6. **VirtualBox** and **Vagrant** - for the VM itself.
7. **Java 10** and **Maven** for building the demo app.

## How to use
1. Add `address=/.kube.dev/192.168.99.100` to `/etc/dnsmasq.conf` 
2. cd to `infrastructure` and run `make setup-certs`
3. cd to `infrastructure` and run `make setup-vm`
4. cd to `infrastructure/kube` and run following commands: 
    1. `make initialize-cluster` - mark master node for being available to scheduling
    2. `make install-secret` and `install-registry-secret` - install TLS and registry auth secrets
    3. `make install-network` - installs Weave networking addon for inter-pod communication
    4. `make install-helm` - installs tiller on the cluster, better go drink a coffee after this as it takes a minute at least, also this is the main reason why there is no `all` target for kube, currently there is no script that would wait for tiller to fully deploy and it has to be deployed because next step uses it to deploy ingress. 
    5. `make install-ingress` - installs ingress-nginx controller, hardcoded to use default (`192.168.99.100` VM IP used in host-only network in VM)
    6. `make install-registry` - installs private docker registry, currently volumes are bugged (probably due to faulty config)
5. cd to project root and run `make push-image` and then `make kube-install`. 

If you want to test your deployment before running demo, go to `https://registry.kube.dev` to verify that domain resolution and TLS termination works correctly.
Also while its not required, you can install CA into your Chrome, for that do following: 
1. Navigate to `chrome://settings/certificates`.
2. Open Authorities tab. 
3. Press import button and select `$projectRoot/infrastructure/certs/output/ca.pem`, click Open.
4. In opened dialog select `Trust this certificate for identifying websites`.
Beware that this will make this certificate considered as completely valid in Chrome.  

Its possible that for some reason DNS resolution will randomly decide not to work after VM provision, in that case you'll have to 
manually edit the /etc/resolv.conf on the machine and add `nameserver 192.168.99.1` to the end of it. This IP of your PC in the VBox network between your VM and your host. 

## Next steps? 
After doing steps 1-4, you can start using your newly created K8S cluster for local development, you can deploy apps and expose them 
through ingress (see demo configs `kubeecho.yaml` and `kubeecho-ingress.yaml`) on virtually any third level `*.kube.dev` subdomain. 

## Acknowledgments
I used this guide to great success to setup CA and server cert generation:
http://fabianlee.org/2018/02/17/ubuntu-creating-a-trusted-ca-and-san-certificate-using-openssl-on-ubuntu/
You can also use generated certs for your own local servers of course, they will be valid for all `*.kube.dev` subdomains (but obviously not 4th level subdomains e.g. not for `something.something.kube.dev`)