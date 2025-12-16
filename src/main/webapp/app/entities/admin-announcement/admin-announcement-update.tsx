import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { AdminAnnouncementType } from 'app/shared/model/enumerations/admin-announcement-type.model';
import { createEntity, getEntity, reset, updateEntity } from './admin-announcement.reducer';

export const AdminAnnouncementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const adminAnnouncementEntity = useAppSelector(state => state.adminAnnouncement.entity);
  const loading = useAppSelector(state => state.adminAnnouncement.loading);
  const updating = useAppSelector(state => state.adminAnnouncement.updating);
  const updateSuccess = useAppSelector(state => state.adminAnnouncement.updateSuccess);
  const adminAnnouncementTypeValues = Object.keys(AdminAnnouncementType);

  const handleClose = () => {
    navigate(`/admin-announcement${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    if (values.announcerId !== undefined && typeof values.announcerId !== 'number') {
      values.announcerId = Number(values.announcerId);
    }

    const entity = {
      ...adminAnnouncementEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          announcementType: 'BANNER',
          ...adminAnnouncementEntity,
          createdDate: convertDateTimeFromServer(adminAnnouncementEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(adminAnnouncementEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.adminAnnouncement.home.createOrEditLabel" data-cy="AdminAnnouncementCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.adminAnnouncement.home.createOrEditLabel">
              Create or edit a AdminAnnouncement
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="admin-announcement-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.adminAnnouncement.announcementType')}
                id="admin-announcement-announcementType"
                name="announcementType"
                data-cy="announcementType"
                type="select"
              >
                {adminAnnouncementTypeValues.map(adminAnnouncementType => (
                  <option value={adminAnnouncementType} key={adminAnnouncementType}>
                    {translate(`monsterdamChicksApp.AdminAnnouncementType.${adminAnnouncementType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.adminAnnouncement.title')}
                id="admin-announcement-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminAnnouncement.content')}
                id="admin-announcement-content"
                name="content"
                data-cy="content"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminAnnouncement.createdDate')}
                id="admin-announcement-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminAnnouncement.lastModifiedDate')}
                id="admin-announcement-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminAnnouncement.createdBy')}
                id="admin-announcement-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminAnnouncement.lastModifiedBy')}
                id="admin-announcement-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminAnnouncement.announcerId')}
                id="admin-announcement-announcerId"
                name="announcerId"
                data-cy="announcerId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/admin-announcement" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AdminAnnouncementUpdate;
