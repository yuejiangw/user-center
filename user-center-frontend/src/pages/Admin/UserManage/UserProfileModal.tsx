import {Image, Modal, Typography} from 'antd';
import {DEFAULT_AVATAR} from "@/common/constants";

const {Text, Title} = Typography;

interface UserProfileModalProps {
  isOpen: boolean,
  handleModalClose: () => void,
  currentRecord: API.CurrentUser | null,
}

const UserProfileModal = ({isOpen, handleModalClose, currentRecord}: UserProfileModalProps) => {
  return (
    <Modal
      title="User Profile"
      open={isOpen}
      onCancel={handleModalClose}
      footer={null}
    >
      {currentRecord && (
        <div style={{textAlign: 'center'}}>
          <Image
            src={currentRecord.avatarUrl || DEFAULT_AVATAR}
            width={100}
            height={100}
            style={{borderRadius: '50%', marginBottom: '20px'}}
          />
          <Title level={4}>{currentRecord.username}</Title>
          <Text>Email: {currentRecord.email}</Text>
          <br/>
          <Text>Phone: {currentRecord.phone}</Text>
          <br/>
          <Text>Gender: {currentRecord.gender === 1 ? 'Male' : 'Female'}</Text>
          <br/>
          <Text>Status: {currentRecord.userStatus === 0 ? 'Active' : 'Deactivated'}</Text>
        </div>
      )}
    </Modal>

  )
}

export default UserProfileModal;
