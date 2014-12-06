#! /bin/sh
### BEGIN INIT INFO
# Provides:          serversocket
# Required-Start:
# Required-Stop:
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Service for access control
# Description:       Controls employee access data
### END INIT INFO
#
# Author:       Thiago Carvalho <tcarvalho@grandev.com.br>
#

PATH=/bin:/usr/bin:/sbin:/usr/sbin
LOG=/var/log/serversocket.log
#PIDFILE=/var/run/serversocket.pid
CLASSPATH=/usr/local/javaclass/postgresql-9.1-901-1.jdbc4.jar:/home/sometal
DAEMON="/usr/lib/jvm/java-openjdk/bin/java -cp $CLASSPATH br/com/grandev/acesso/ServerSocket --debug"
#/usr/local/jdk1.7/bin/java

# /hoest -x $DAEMON || exit 0

#
# fix init-functions and log_daemon
#

	if [ -e /etc/debian_version ]; then
	    . /lib/lsb/init-functions
	elif [ -e /etc/init.d/functions ] ; then
	    . /etc/init.d/functions
	fi
	
	if ! type log_daemon_msg 2>&1 |grep -qai function ; then
	# no lsb setup?  no problem.  we will add them in
	
	log_use_fancy_output () {
	    TPUT=/usr/bin/tput
	    EXPR=/usr/bin/expr
	    if [ -t 1 ] && [ "x$TERM" != "" ] && [ "x$TERM" != "xdumb" ] && [ -x $TPUT ] && [ -x $EXPR ] && $TPUT hpa 60 >/dev/null 2>&1 && $TPUT setaf 1 >/dev/null 2>&1; then
	        [ -z $FANCYTTY ] && FANCYTTY=1 || true
	    else
	        FANCYTTY=0
	    fi
	    case "$FANCYTTY" in
	        1|Y|yes|true)   true;;
	        *)              false;;
	    esac
	}
	
	log_success_msg () {
	    if [ -n "${1:-}" ]; then
	        log_begin_msg $@
	    fi
	    log_end_msg 0
	}
	
	log_failure_msg () {
	    if [ -n "${1:-}" ]; then
	        log_begin_msg $@
	    fi
	    log_end_msg 1 || true
	}
	
	log_warning_msg () {
	    if [ -n "${1:-}" ]; then
	        log_begin_msg $@
	    fi
	    log_end_msg 255 || true
	}
	
	#
	# NON-LSB HELPER FUNCTIONS
	#
	# int get_lsb_header_val (char *scriptpathname, char *key)
	get_lsb_header_val () {
	        if [ ! -f "$1" ] || [ -z "${2:-}" ]; then
	                return 1
	        fi
	        LSB_S="### BEGIN INIT INFO"
	        LSB_E="### END INIT INFO"
	        sed -n "/$LSB_S/,/$LSB_E/ s/# $2: \(.*\)/\1/p" $1
	}
	
	# int log_begin_message (char *message)
	log_begin_msg () {
	    if [ -z "${1:-}" ]; then
	        return 1
	    fi
	    echo -n "$@"
	}
	
	# Sample usage:
	# log_daemon_msg "Starting GNOME Login Manager" "gdm"
	#
	# On Debian, would output "Starting GNOME Login Manager: gdm"
	# On Ubuntu, would output " * Starting GNOME Login Manager..."
	#
	# If the second argument is omitted, logging suitable for use with
	# log_progress_msg() is used:
	#
	# log_daemon_msg "Starting remote filesystem services"
	#
	# On Debian, would output "Starting remote filesystem services:"
	# On Ubuntu, would output " * Starting remote filesystem services..."
	
	log_daemon_msg () {
	    if [ -z "${1:-}" ]; then
	        return 1
	    fi
	    log_daemon_msg_pre "$@"
	
	    if [ -z "${2:-}" ]; then
	        echo -n "$1:"
	        return
	    fi
	
	    echo -n "$1: $2"
	    log_daemon_msg_post "$@"
	}
	
	# #319739
	#
	# Per policy docs:
	#
	#     log_daemon_msg "Starting remote file system services"
	#     log_progress_msg "nfsd"; start-stop-daemon --start --quiet nfsd
	#     log_progress_msg "mountd"; start-stop-daemon --start --quiet mountd
	#     log_progress_msg "ugidd"; start-stop-daemon --start --quiet ugidd
	#     log_end_msg 0
	#
	# You could also do something fancy with log_end_msg here based on the
	# return values of start-stop-daemon; this is left as an exercise for
	# the reader...
	#
	# On Ubuntu, one would expect log_progress_msg to be a no-op.
	log_progress_msg () {
	    if [ -z "${1:-}" ]; then
	        return 1
	    fi
	    echo -n " $@"
	}
	
	# int log_end_message (int exitstatus)
	log_end_msg () {
	    # If no arguments were passed, return
	    if [ -z "${1:-}" ]; then
	        return 1
	    fi
	
	    retval=$1
	
	    log_end_msg_pre "$@"
	
	    # Only do the fancy stuff if we have an appropriate terminal
	    # and if /usr is already mounted
	    if log_use_fancy_output; then
	        RED=`$TPUT setaf 1`
	        YELLOW=`$TPUT setaf 3`
	        NORMAL=`$TPUT op`
	    else
	        RED=''
	        YELLOW=''
	        NORMAL=''
	    fi
	
	    if [ $1 -eq 0 ]; then
	        echo "."
	    elif [ $1 -eq 255 ]; then
	        /bin/echo -e " ${YELLOW}(warning).${NORMAL}"
	    else
	        /bin/echo -e " ${RED}failed!${NORMAL}"
	    fi
	    log_end_msg_post "$@"
	    return $retval
	}
	
	log_action_msg () {
	    echo "$@."
	}
	
	log_action_begin_msg () {
	    echo -n "$@..."
	}
	
	log_action_cont_msg () {
	    echo -n "$@..."
	}
	
	log_action_end_msg () {
	    log_action_end_msg_pre "$@"
	    if [ -z "${2:-}" ]; then
	        end="."
	    else
	        end=" ($2)."
	    fi
	
	    if [ $1 -eq 0 ]; then
	        echo "done${end}"
	    else
	        if log_use_fancy_output; then
	            RED=`$TPUT setaf 1`
	            NORMAL=`$TPUT op`
	            /bin/echo -e "${RED}failed${end}${NORMAL}"
	        else
	            echo "failed${end}"
	        fi
	    fi
	    log_action_end_msg_post "$@"
	}
	
	# Hooks for /etc/lsb-base-logging.sh
	log_daemon_msg_pre () { :; }
	log_daemon_msg_post () { :; }
	log_end_msg_pre () { :; }
	log_end_msg_post () { :; }
	log_action_end_msg_pre () { :; }
	log_action_end_msg_post () { :; }
	
	fi

#
# fix init-functions and log_daemon
#

getpidserversocket(){
  PID=`ps ax --format pid,cmd | grep java | grep ServerSocket | grep -v "su grandevc"`
  OLDIFS=$IFS
  IFS=" "
  pid=
  for pid in $PID
  do
    break
  done
  IFS=$OLDIFS
}

case "$1" in
  start)
        log_daemon_msg "Starting ServerSocket"
        #start_daemon -p $PIDFILE $DAEMON
        su grandevc -c "$DAEMON" >> $LOG 2>&1 &
        log_end_msg $?
    ;;
  stop)
        log_daemon_msg "Stopping ServerSocket"
        getpidserversocket
        kill -TERM $pid
        log_end_msg $?
    ;;
  kill)
       log_daemon_msg "Killing ServerSocket"
        getpidserversocket
        kill -KILL $pid
        log_end_msg $?
    ;;
  force-reload|restart)
    $0 stop
    $0 start
    ;;
  *)
    echo "Usage: /etc/init.d/serversocket {start|stop|kill|restart|force-reload}"
    exit 1
    ;;
esac

exit 0
