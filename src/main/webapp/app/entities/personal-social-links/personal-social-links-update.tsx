import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getSocialNetworks } from 'app/entities/social-network/social-network.reducer';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { createEntity, getEntity, reset, updateEntity } from './personal-social-links.reducer';

export const PersonalSocialLinksUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const socialNetworks = useAppSelector(state => state.socialNetwork.entities);
  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const personalSocialLinksEntity = useAppSelector(state => state.personalSocialLinks.entity);
  const loading = useAppSelector(state => state.personalSocialLinks.loading);
  const updating = useAppSelector(state => state.personalSocialLinks.updating);
  const updateSuccess = useAppSelector(state => state.personalSocialLinks.updateSuccess);

  const handleClose = () => {
    navigate(`/personal-social-links${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSocialNetworks({}));
    dispatch(getUserProfiles({}));
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
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...personalSocialLinksEntity,
      ...values,
      socialNetwork: socialNetworks.find(it => it.id.toString() === values.socialNetwork?.toString()),
      userProfile: userProfiles.find(it => it.id.toString() === values.userProfile?.toString()),
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
          deletedDate: displayDefaultDateTime(),
        }
      : {
          ...personalSocialLinksEntity,
          createdDate: convertDateTimeFromServer(personalSocialLinksEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(personalSocialLinksEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(personalSocialLinksEntity.deletedDate),
          socialNetwork: personalSocialLinksEntity?.socialNetwork?.id,
          userProfile: personalSocialLinksEntity?.userProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.personalSocialLinks.home.createOrEditLabel" data-cy="PersonalSocialLinksCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.personalSocialLinks.home.createOrEditLabel">
              Create or edit a PersonalSocialLinks
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
                  id="personal-social-links-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.personalSocialLinks.normalImageS3Key')}
                id="personal-social-links-normalImageS3Key"
                name="normalImageS3Key"
                data-cy="normalImageS3Key"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.personalSocialLinks.thumbnailIconS3Key')}
                id="personal-social-links-thumbnailIconS3Key"
                name="thumbnailIconS3Key"
                data-cy="thumbnailIconS3Key"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.personalSocialLinks.socialLink')}
                id="personal-social-links-socialLink"
                name="socialLink"
                data-cy="socialLink"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.personalSocialLinks.createdDate')}
                id="personal-social-links-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.personalSocialLinks.lastModifiedDate')}
                id="personal-social-links-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.personalSocialLinks.createdBy')}
                id="personal-social-links-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.personalSocialLinks.lastModifiedBy')}
                id="personal-social-links-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.personalSocialLinks.deletedDate')}
                id="personal-social-links-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="personal-social-links-socialNetwork"
                name="socialNetwork"
                data-cy="socialNetwork"
                label={translate('monsterdamChicksApp.personalSocialLinks.socialNetwork')}
                type="select"
              >
                <option value="" key="0" />
                {socialNetworks
                  ? socialNetworks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="personal-social-links-userProfile"
                name="userProfile"
                data-cy="userProfile"
                label={translate('monsterdamChicksApp.personalSocialLinks.userProfile')}
                type="select"
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/personal-social-links" replace color="info">
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

export default PersonalSocialLinksUpdate;
